package ImageHidingCode;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageHiding extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private BufferedImage hostImage;
    private BufferedImage secretImage;
    JPanel controlPanel;
    JPanel imagePanel;
    private JComboBox<String> operationSelector;
    
    private JButton processLSBInMSBButton;
    private JButton processLSBInLSBButton;
    JTextField encodeBitsText;
    JButton encodeBitsPlus;
    JButton encodeBitsMinus;
    JTextField nBitsText;
    JButton nBitsPlus;
    JButton nBitsMinus;
    ImageCanvas hostCanvas;
    ImageCanvas secretCanvas;
    private Steganography s;
    
    public ImageHiding() {
    	
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setTitle("Image Hiding Demo");
        Container container = this.getContentPane();

        this.setLayout(layout);

        this.add(new JLabel("Bits to encode into host image:"));

        encodeBitsText = new JTextField("0", 5);
        encodeBitsText.setEditable(false);

        gbc.weightx = -1.0;
        layout.setConstraints(encodeBitsText, gbc);
        this.add(encodeBitsText);

        encodeBitsPlus = new JButton("+");
        encodeBitsPlus.addActionListener(this);

        encodeBitsMinus = new JButton("-");
        encodeBitsMinus.addActionListener(this);

        gbc.weightx = 1.0;
        layout.setConstraints(encodeBitsPlus, gbc);
        this.add(encodeBitsPlus);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(encodeBitsMinus, gbc);
        this.add(encodeBitsMinus);

        GridBagLayout imageGridbag = new GridBagLayout();
        GridBagConstraints imageGBC = new GridBagConstraints();

        imagePanel = new JPanel();
        imagePanel.setLayout(imageGridbag);

        JLabel hostImageLabel = new JLabel("Host image:");
        JLabel secretImageLabel = new JLabel("Secret image:");

        imagePanel.add(hostImageLabel);

        imageGBC.gridwidth = GridBagConstraints.REMAINDER;
        imageGridbag.setConstraints(secretImageLabel, imageGBC);
        imagePanel.add(secretImageLabel);

        hostCanvas = new ImageCanvas(this.getHostImage());  
        secretCanvas = new ImageCanvas(this.getSecretImage());

        imagePanel.add(hostCanvas);
        imagePanel.add(secretCanvas);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(imagePanel, gbc);
        this.add(imagePanel);

        Steganography host = new Steganography(this.getHostImage());
        host.encode(this.getSecretImage(), this.getBits());
        hostCanvas.setImage(host.getImage());

        Steganography secret = new Steganography(this.getSecretImage());
        secret.getMaskedImage(this.getBits());
        secretCanvas.setImage(secret.getImage());

        
        this.setLayout(layout);
        operationSelector = new JComboBox<>(new String[]{
                "Conceal MSB of S in MSB of H",
                "Conceal LSB of S in LSB of H",
                "Conceal LSB of S in MSB of H"
        });
        processLSBInMSBButton = new JButton("Process LSB in MSB");
        processLSBInMSBButton.addActionListener(this);
        gbc.weightx = 1.0;
        layout.setConstraints(processLSBInMSBButton, gbc);
        this.add(processLSBInMSBButton);

        processLSBInLSBButton = new JButton("Process LSB in LSB");
        processLSBInLSBButton.addActionListener(this);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(processLSBInLSBButton, gbc);
        this.add(processLSBInLSBButton);

        hostCanvas = new ImageCanvas(getHostImage());
        this.add(hostCanvas);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public BufferedImage getHostImage() {
        // Load host image logic
    	BufferedImage img = null;

      try
   	  {
   	   img = ImageIO.read(new File("/Users/kl/dev/ICSI426/ICSI426_HW3/HW3/src/host_image.jpg"));
    	  }
    	  catch (IOException ioe) { ioe.printStackTrace(); }

    	  return img;
    }

    public BufferedImage getSecretImage()
   	 {
    	BufferedImage img = null;

      try
   	  {
   	   img = ImageIO.read(new File("/Users/kl/dev/ICSI426/ICSI426_HW3/HW3/src/secret_image.jpg"));
   	  }
    	 catch (IOException ioe) { ioe.printStackTrace(); }

    	 return img;
     }
       
    
    public int getBits()
    {
    	return Integer.parseInt(encodeBitsText.getText());
    }
   

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == processLSBInMSBButton || source == processLSBInLSBButton) {
            String selectedOperation = (String) operationSelector.getSelectedItem();
            s = new Steganography(getHostImage());

            switch (selectedOperation) {
                case "Conceal MSB of S in MSB of H":
                    s.concealMSBInMSB(getSecretImage());
                    break;
                case "Conceal LSB of S in LSB of H":
                    s.concealLSBInLSB(getSecretImage());
                    break;
                case "Conceal LSB of S in MSB of H":
                    s.concealLSBInMSB(getSecretImage());
                    break;
            }

           hostCanvas.setImage(s.getImage());
           hostCanvas.repaint();

           
       }
        
        if (source == encodeBitsPlus)
        {
         int bits = this.getBits() + 1;

         if (bits > 8) { bits = 8; }

         encodeBitsText.setText(Integer.toString(bits));

         s = new Steganography(this.getHostImage());
         s.encode(this.getSecretImage(), bits);

         hostCanvas.setImage(s.getImage());
         hostCanvas.repaint();

         s = new Steganography(this.getSecretImage());
         s.getMaskedImage(bits);

         secretCanvas.setImage(s.getImage());
         secretCanvas.repaint();
        }
        else if (source == encodeBitsMinus)
        {
         int bits = this.getBits() - 1;

         if (bits < 0) { bits = 0; }

         encodeBitsText.setText(Integer.toString(bits));
        
         s = new Steganography(this.getHostImage());
         s.encode(this.getSecretImage(), bits);
        }
         
    
    }

    public static void main(String[] args) {
        new ImageHiding();
    }

    public class ImageCanvas extends JPanel {
        private static final long serialVersionUID = 1L;

        private Image img;

        public ImageCanvas(Image img) {
            this.img = img;
            this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        }

        public void setImage(Image img) {
            this.img = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }
    }
}

class Steganography {
    private BufferedImage image;
    public void getMaskedImage(int bits)
    {
     int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

     int maskBits = (int)(Math.pow(2, bits)) - 1 << (8 - bits);
     int mask = (maskBits << 24) | (maskBits << 16) | (maskBits << 8) | maskBits;

     for (int i = 0; i < imageRGB.length; i++)
     {
      imageRGB[i] = imageRGB[i] & mask;
     }

     image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
    }
    public void concealMSBInMSB(BufferedImage secretImage) {
        int[] secretRGB = secretImage.getRGB(0, 0, secretImage.getWidth(), secretImage.getHeight(), null, 0, secretImage.getWidth());
        int[] hostRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < hostRGB.length; i++) {
            int secretMSB = (secretRGB[i] & 0xFF000000) >>> 24;
            hostRGB[i] = (hostRGB[i] & 0x00FFFFFF) | (secretMSB << 24);
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), hostRGB, 0, image.getWidth());
    }
    
    public void concealLSBInMSB(BufferedImage secretImage) {
        int[] secretRGB = secretImage.getRGB(0, 0, secretImage.getWidth(), secretImage.getHeight(), null, 0, secretImage.getWidth());
        int[] hostRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < hostRGB.length; i++) {
            int secretLSB = (secretRGB[i] & 0x000000FF) << 24;
            hostRGB[i] = (hostRGB[i] & 0x00FFFFFF) | secretLSB;
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), hostRGB, 0, image.getWidth());
    }

    public void concealLSBInLSB(BufferedImage secretImage) {
        int[] secretRGB = secretImage.getRGB(0, 0, secretImage.getWidth(), secretImage.getHeight(), null, 0, secretImage.getWidth());
        int[] hostRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < hostRGB.length; i++) {
            int secretLSB = secretRGB[i] & 0x000000FF;
            hostRGB[i] = (hostRGB[i] & 0xFFFFFF00) | secretLSB;
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), hostRGB, 0, image.getWidth());
    }
    
    public void encode(BufferedImage encodeImage, int encodeBits)
    {
     int[] encodeRGB = encodeImage.getRGB(0, 0, encodeImage.getWidth(null), encodeImage.getHeight(null), null, 0, encodeImage.getWidth(null));
     int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

     int encodeByteMask = (int)(Math.pow(2, encodeBits)) - 1 << (8 - encodeBits);
     int encodeMask = (encodeByteMask << 24) | (encodeByteMask << 16) | (encodeByteMask << 8) | encodeByteMask;

     int decodeByteMask = ~(encodeByteMask >>> (8 - encodeBits)) & 0xFF;
     int hostMask = (decodeByteMask << 24) | (decodeByteMask << 16) | (decodeByteMask << 8) | decodeByteMask;

     for (int i = 0; i < imageRGB.length; i++)
     {
      int encodeData = (encodeRGB[i] & encodeMask) >>> (8 - encodeBits);
      imageRGB[i] = (imageRGB[i] & hostMask) | (encodeData & ~hostMask);
     }

     image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
    }
    
    public Image getImage() {
        return image;
    }

    public Steganography(BufferedImage image) {
        this.image = image;
    }
}