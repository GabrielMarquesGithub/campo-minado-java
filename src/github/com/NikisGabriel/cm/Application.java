package github.com.NikisGabriel.cm;

import github.com.NikisGabriel.cm.model.Tabuleiro;
import github.com.NikisGabriel.cm.view.TabuleiroConsole;

public class Application {

	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(6, 6, 6);

		TabuleiroConsole console = new TabuleiroConsole(tabuleiro);

	}

}
