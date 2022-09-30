package github.com.NikisGabriel.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import github.com.NikisGabriel.cm.exeption.ExplosionException;

public class Tabuleiro {
	private int rows;
	private int columns;
	private int mines;

	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;

		// métodos para iniciação sendo chamados diretamente no construtor
		createCampos();
		associateNeighbors();
		drawMines();
	}

	// função para abrir campos
	public void openChosenField(int row, int column) {
		// predicado para encontrar não posição
		Predicate<Campo> comparePositions = c -> c.getRow() == row && c.getColumn() == column;

		try {
			// filtrando e depois retornando o primeiro elemento
			campos.parallelStream().filter(comparePositions).findFirst().ifPresent(c -> c.openCampo());
		} catch (ExplosionException e) {
			campos.forEach(c -> c.setOpen(true));

			// lançando o erro novamente pois é necessário no nível superior
			throw e;
		}

	}

	// função para marcar
	public void markedChosenField(int row, int column) {
		Predicate<Campo> comparePositions = c -> c.getRow() == row && c.getColumn() == column;
		campos.parallelStream().filter(comparePositions).findFirst().ifPresent(c -> c.toggleChecked());
	}

	private void createCampos() {
		// iterando sobre as linhas
		for (int row = 0; row < this.rows; row++) {
			// iterando sobre as colunas
			for (int column = 0; column < this.columns; column++) {
				// criando os campos como lista e não matrix
				campos.add(new Campo(row, column));
			}
		}
	}

	private void associateNeighbors() {
		// realizando duas leituras, a lógica para adição ou não de vizinhos está dentro
		// da classe campo
		for (Campo c : campos) {
			for (Campo n : campos) {
				c.addNeighbors(n);
			}
		}
	}

	private void drawMines() {
		long armedMines = 0;
		// Criando predicado fora para facilitar leitura
		Predicate<Campo> mined = c -> c.isMined();

		do {
			// a cada ciclo é feito a contagem de minas
			armedMines = this.campos.stream().filter(mined).count();
			// criando uma valor aleatório com no máximo o tamanho do campo
			int random = (int) (Math.random() * campos.size());
			// pegando um campo aleatório escolhido e colocando mina nele
			campos.get(random).undermine();

		} while (armedMines < this.mines);
	}

	public boolean concluded() {
		// se todos os campos estão resolvidos então o jogo foi ganho
		return this.campos.stream().allMatch(c -> c.concluded());
	}

	public void resetGame() {
		// resetando todos os campos
		this.campos.stream().forEach(c -> c.reset());
		drawMines();
	}

	@Override
	public String toString() {
		// criando um StringBuilder
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		// ciclo para criar o index
		for (int column = 0; column < this.columns; column++) {
			sb.append(" ");
			sb.append(column);
			sb.append(" ");
		}
		sb.append("\n");

		// criando um index externo
		int i = 0;
		for (int row = 0; row < this.rows; row++) {
			// pulando linhas
			sb.append(row + " ");
			for (int column = 0; column < this.columns; column++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");

				i++;
			}
			sb.append("\n");
		}
		// convertendo para string
		return sb.toString();
	}

}
