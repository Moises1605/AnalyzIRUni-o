package br.uefs.analyzIR.ui.common;
//Bibliotecas

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


//Classe SplashScreen, que extende da JFrame
public class SplashScreen extends JFrame
{
    private static JLabel lbSplash;          //label responsável por conter a imagem
    private ImageIcon imSplash;              //imagem que será mostrada no label

    //função principal que será carregada
    public void showSplash(){
        //Criando a janela de view.Splash
        SplashScreen s = new SplashScreen();


        //definindo o tipo de fechamento, o tamanho, tirando a barra de títulos, deixando no centro, definindo um icone e mostrando a janela
        s.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        s.setSize(416,272);
        s.setUndecorated(true);
        s.setLocationRelativeTo(null);
        s.setVisible(true);
        //s.setOpacity(0.7f);

        //forçando a espera de 1500 milissegundos (1,5 segundos)
        try {
            Thread.sleep (1500);
        }
        catch (InterruptedException ex) {}

        //fechando a janela
        s.dispose();
    }

    //função responsável por carregar os dados da janela (layout e imagem)
    public SplashScreen() {
        //definindo o layout como nulo
        setLayout(null);

        //setando a imagem de splash
        imSplash = new ImageIcon(getClass().getResource("/logo.png"));


        //adicionando a imagem no label e mudando o tamanho
        lbSplash = new JLabel(imSplash);
        lbSplash.setBounds(0,0,416,272);

        //adicionando componentes na janela
        add(lbSplash);
    }

    //função ao fechar a splash
    public void dispose(){

        super.dispose();
    }

}