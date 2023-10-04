package util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class Thumbnail {
    
    
    private String imgOriginal;
    private String imgResult;
    private int calidad;
    private int width;
    private int height;
    private Image img;

    public Thumbnail(Image img,String result) {
        this.img = img;
        imgResult = result;
    }
    
    
    
    public void resize()throws Exception{
        //Cargamos la imagen y la preparamos para manipularla
        Image image = Toolkit.getDefaultToolkit().getImage(imgOriginal);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        mediaTracker.waitForID(0);
        //Sacamos la proporcion de las nuevas medidas
        double tnsPro = (double)width/(double)height;
        //Calculamos la proporcion de la imagen original
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        double imgPro = (double)imgWidth / (double)imgHeight;
        //comprobamos cual de las dos medidas introducidas esta desproporcionada
        //para asi corregirla
        if (tnsPro < imgPro) {
            
            height = (int)(width / imgPro);
        } else {
            //lo mismo pero con el width
            width = (int)(height * imgPro);
        }
        
        
    }
    
    public FileInputStream generarThumbnail()
    {
        BufferedImage tnsImg = new BufferedImage(img.getWidth(null),img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = tnsImg.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
        
        //Preparamos para escribir la imagen generada en el disco
        BufferedOutputStream out;
        try {
            out = new BufferedOutputStream(new FileOutputStream(imgResult));            
            
            ImageIO.write(tnsImg, "jpg", out);
            
        out.close(); 
        return new FileInputStream(imgResult);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Thumbnail.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex){}
        
        return null;
        
    }
    
    
    
    public int getCalidad() {
        return calidad;
    }
   
    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public String getImgOriginal() {
        return imgOriginal;
    }
   
    public void setImgOriginal(String imgOriginal) {
        this.imgOriginal = imgOriginal;
    }
    
    public String getImgResult() {
        return imgResult;
    }
    
    public void setImgResult(String imgResult) {
        this.imgResult = imgResult;
    }
   
    public int getWidth() {
        return width;
    }
  
    public void setWidth(int width) {
        this.width = width;
    }
    
}
