package br.uefs.analyzIR.ui.common;

import br.uefs.analyzIR.controller.Facade;

import java.io.IOException;

public class Application {

	public static void main(String [] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		
		Facade facade = new Facade();
		ProjectView project = ProjectView.getInstance(facade);
		new SplashScreen().showSplash();
		project.show();

	}
}
