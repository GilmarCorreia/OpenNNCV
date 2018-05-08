%% ABRINDO AS IMAGENS
clear
clc

load('dados2.mat')

heigth = 160;
width = 120;

inicio = 59;
fim = 310;%321
trainPic = (fim-inicio)+1;
total = (321-inicio)+1;

reduz = 10;


Passar1 = zeros((width/reduz)*(heigth/reduz),total,(reduz*reduz));
ANNimagens = zeros((width/reduz)*(heigth/reduz),total*(reduz*reduz));
ANNlabels = dados2;
ANNvalidations = zeros(20,total+1);
pasta='C:\Users\Gilmar Correia\Documents\Documentos\Projetos\GitHub\RNA-OpenCV\Licoes\LaplacianFilter\';
%pasta = '/Users/junior/Desktop/GitHub/RNA-OpenCV/Licoes/LaplacianFilter/';

imagem=1;
t = 1;
for i = inicio:321
    Pic1 = imread(strcat(pasta,'BCG',int2str(i),'.jpg'));
    a = 0;
    b = 0;
    for parte = 1:(reduz*reduz)
        akk1 = zeros((width/reduz),(heigth/reduz));
        if b<reduz
            akk1 = Pic1((1+(a*(width/reduz))):((a+1)*(width/reduz)),(1+(b*(heigth/reduz))):((b+1)*(heigth/reduz)));
            Passar1(:,imagem,parte) = reshape(akk1,(width/reduz)*(heigth/reduz),1);
            b = b + 1;
        else
            a = a + 1;
            b = 0;
            akk1 = Pic1((1+(a*(width/reduz))):((a+1)*(width/reduz)),(1+(b*(heigth/reduz))):((b+1)*(heigth/reduz)));
            Passar1(:,imagem,parte) = reshape(akk1,(width/reduz)*(heigth/reduz),1);
            b= b +1;
        end   
        ANNimagens(:,t) = reshape(Passar1(:,imagem,parte),(width/reduz)*(heigth/reduz),1);
        t = t + 1;
    end
    imagem = imagem +1;
end

% imagem = 2; 
% akk2 = uint8(reshape(ANNimagens(:,(((imagem-1)*(reduz*reduz))+1):(imagem*reduz*reduz)),width/reduz,heigth/reduz,(reduz*reduz)));
% t = 1;
% for i = 1:reduz
%     for j = 1:reduz
%         figure(1);
%         subplot('Position',[(j-1)*1/reduz (reduz-i)*1/reduz 1/reduz 1/reduz]),imshow(akk2(:,:,t));
%         t = t +1;
%     end
% end

%% TREINANDO O FILTRO

for r = 1
    netFilter = feedforwardnet(r);
    netFilter.trainParam.max_fail = 100;
    netFilter.trainParam.time = 360;
    
    netFilter = train(netFilter,ANNimagens(:,1:(trainPic*reduz*reduz)),ANNlabels(1:(trainPic)*(reduz*reduz))');
    
%% VALIDANDO FILTRO COM IMAGENS
    imagem = 249; 
    akk2 = uint8(reshape(ANNimagens(:,(((imagem-1)*(reduz*reduz))+1):(imagem*reduz*reduz)),width/reduz,heigth/reduz,(reduz*reduz)));
    for i = 1:(reduz*reduz)
        figure(r);
        subplot(reduz,reduz,i),imshow(akk2(:,:,i));
        teste = string(round(sim(netFilter,double(reshape(akk2(:,:,i),(width/reduz)*(heigth/reduz),1)) ) ) );
        title(teste);
    end

%% VALIDANDO FILTRO COM DADOS
    ANNvalidations(r+1,1) = r;
    t = 1;
    for i = 1:total
        ANNvalidations(1,i+1) = i;
        for j = 1:(reduz*reduz)
            if(round(sim(netFilter,ANNimagens(:,t))) == ANNlabels(t))
                ANNvalidations(r+1,i+1) = ANNvalidations(r+1,i+1) + 1;
            end
            t = t + 1;
        end
        ANNvalidations(r+1,i+1) = (reduz*reduz) - ANNvalidations(r+1,i+1);
    end
end
