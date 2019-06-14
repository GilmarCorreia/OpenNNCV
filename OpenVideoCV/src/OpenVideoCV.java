import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencvext.matrix.MatrixOpenCV_Ext;

public class OpenVideoCV extends MatrixOpenCV_Ext{
	
	private static JFrame frame;
	private static JLabel imageLabel;
	private static VideoCapture capture;
	private static int width = 160, heigth = 120;
	
	private static int imgNumber = 1;
	private static String folder = "C:\\Users\\Gilmar Jeronimo\\Desktop\\Testes MATLAB\\DataBase\\Backup";

	public static void main(String[] args) throws InterruptedException {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		/* 
		 * A primeira webcam conectada tem valor 0 no initGUI, 
		 * a segunda tem valor 1, e assim por diante...
		 */
		initGUI(0); 
		
		// Define a matriz que vai ser mostrada na TELA.
		Mat webcamMatImage = new Mat();
		
		// Inicia o tempo do sistema.
		long initial = System.nanoTime()/1000000;
		
		// Se achou a webcam conectada ir� mostrar na tela
		if(capture.isOpened()){
			while (true){  
				// A imagem capturada na webcam � salva em webcamMatImage
				capture.read(webcamMatImage);				

				// Uma c�pia de webcamMatImage � feita
				Mat imagem = webcamMatImage.clone();
			    
				//[Coloque seu c�digo aqui...] - Exemplo:
				//imagem = matGrayscale_Luminosity(imagem);
				//saveOpponentImages(imagem,initial);
				
				
				
				
				// Mostra a imagem
				matVideo(frame, imageLabel,imagem);
				
				// Pausa por 10 segundos
				//Thread.sleep(10000);				
			}  
		}
		else{
			System.out.println("Couldn't open capture.");
		}
	}
	
	/*
	 * O c�digo para captura de tela em formato de v�deo e a classe ImageViewer 
	 * � baseado no c�digo de: https://
	 */
	public static void initGUI(int camNumber) {
		frame = new JFrame("Camera Input Example");  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.setSize(width,heigth);  
		imageLabel = new JLabel();
		frame.add(imageLabel);
		frame.setVisible(true);    
		
		capture = new VideoCapture(camNumber);
		
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH,width);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,heigth);	
	}
	
	/*
	 * C�digo utilizado para salvar as imagens dos oponentes. Desatualizado.
	 */
	public static void saveOpponentImages(Mat imagem,long initial) {
		long finals = System.nanoTime()/1000000;
		long time = finals - initial;
		
		if(time>=6000) {
			String pos = Integer.toString(imgNumber);
			String pasta = folder+"\\"+"Test\\";
			
			Imgcodecs.imwrite(pasta+"C_"+pos+".jpg",imagem);
			
			matBC(imagem,1.1,20);
			Imgcodecs.imwrite(pasta+"BC_C"+pos+".jpg",imagem);
			
			imagem = matGrayscale_Luminosity(imagem);
			Imgcodecs.imwrite(pasta+"BCG_C"+pos+".jpg",imagem);
			
			Imgproc.Laplacian(imagem,imagem, CvType.CV_8UC1, 3, (double) 3.5, (double) 1.5);
			Imgcodecs.imwrite(pasta+"BCGL_C"+pos+".jpg",imagem);
			
			imgNumber++;
			
			initial = System.nanoTime()/1000000;
			
			System.out.println(pos+".jpg");
		}
	}

}
