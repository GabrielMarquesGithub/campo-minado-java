package github.com.NikisGabriel.cm.view;

import java.util.Scanner;

import github.com.NikisGabriel.cm.exeption.ExitException;
import github.com.NikisGabriel.cm.exeption.ExplosionException;
import github.com.NikisGabriel.cm.model.Tabuleiro;

public class TabuleiroConsole {

	private Tabuleiro tabuleiro;
	private Scanner key = new Scanner(System.in);

	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;

		runGame();
	}

	private void runGame() {
		try {
			boolean execute = true;

			while (execute) {
				// execução da gameplay
				gameCycle();

				System.out.println("Jogar outra partida (S/n) ");
				String res = this.key.nextLine();

				// Lógica para sair ou jogar novamente
				if ("n".equalsIgnoreCase(res)) {
					execute = false;
				} else {
					this.tabuleiro.resetGame();
				}

			}

		} catch (ExitException e) {
			System.out.println("Tchau!!!");
		} finally {
			this.key.close();
		}
	}

	private void gameCycle() {
		try {

			// enquanto o objetivo do tabuleiro não foi alcançado ele vai executar
			while (!this.tabuleiro.concluded()) {
				System.out.println(this.tabuleiro);

				// pedindo a posição
				String typed = writingObserver("Digite (x, y)");

				// separando os valores da posição em row e column
				String[] StringValues = typed.split(",");
				int rowValue = Integer.parseInt(StringValues[0].trim());
				int columnValue = Integer.parseInt(StringValues[1].trim());

				// definindo a operação
				typed = writingObserver("1 - para abrir ou 2 - (Des)Marcar");
				if ("1".equals(typed)) {
					tabuleiro.openChosenField(rowValue, columnValue);
				}
				if ("2".equals(typed)) {
					tabuleiro.markedChosenField(rowValue, columnValue);
				}

			}

			System.out.println(this.tabuleiro);
			System.out.println("Você ganhou!!!");
		} catch (ExplosionException e) {
			System.out.println(this.tabuleiro);
			System.out.println("Você perdeu!!!");

		}
	}

	// método para reutilizar lógica de digitação
	private String writingObserver(String text) {
		System.out.print(text);
		String typed = key.nextLine();

		if ("sair".equalsIgnoreCase(typed)) {
			throw new ExitException();
		}
		return typed;
	}

}
