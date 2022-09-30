package github.com.NikisGabriel.cm.model;

import java.util.ArrayList;
import java.util.List;

import github.com.NikisGabriel.cm.exeption.ExplosionException;

public class Campo {
	private final int row;
	private final int column;

	// por padrão valores boolean começam com false
	private boolean open;
	private boolean mined;
	private boolean marked;

	// cada campo armazena seus vizinhos
	private List<Campo> neighbors = new ArrayList<>();

	// visibilidade de pacote
	Campo(int row, int column) {
		this.row = row;
		this.column = column;
	}

	// retorna true caso ocorra a adição
	boolean addNeighbors(Campo neighbor) {
		// verificando se o elemento está na diagonal
		boolean diagonal = this.row != neighbor.row && this.column != neighbor.column;

		// calculando a distancia entre os campos
		int rowDistance = Math.abs(this.row - neighbor.row);
		int columnDistance = Math.abs(this.column - neighbor.column);
		int distance = rowDistance + columnDistance;

		// validando se o campo está na diagonal
		if (diagonal) {
			// se na diagonal deve esta a 2 de distancia
			if (distance == 2) {
				this.neighbors.add(neighbor);
				return true;
			}
			return false;
		}
		// se não estiver na diagonal deve esta a 1 de distancia
		if (distance == 1) {
			this.neighbors.add(neighbor);
			return true;
		}
		return false;
	}

	void toggleChecked() {
		if (!this.open) {
			this.marked = !this.marked;
		}
	}

	void undermine() {
		this.mined = true;
	}

	boolean openCampo() {
		// programação defensiva impedindo a ação de abrir
		if (this.marked || this.open) {
			return false;
		}
		this.open = true;

		// alterando o fluxo da execução lançando uma exceção
		if (mined) {
			throw new ExplosionException("Fim de jogo");
		}

		// caso a vizinhança for segura todos serão abertos
		if (safeNeighborhood()) {
			neighbors.forEach(n -> n.openCampo());
		}

		return true;
	}

	// teste para saber se a vizinhança é segura
	boolean safeNeighborhood() {
		return this.neighbors.stream().noneMatch(n -> n.mined);
	}

	// métodos get
	public boolean isMarked() {
		return this.marked;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return this.open;
	}

	public boolean isClose() {
		return !this.isOpen();
	}

	public boolean isMined() {
		return this.mined;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	// retorna objetivos dos campos
	boolean concluded() {
		// o campo quando não minado está resolvido quando estiver aberto
		boolean compoUnraveled = this.open && !this.mined;
		// quando tem mina ele é resolvido quando estiver marcado
		boolean campoProtected = this.marked && this.mined;

		return compoUnraveled || campoProtected;
	}

	// método para checar quantidades de minas próximas
	long nearbyMines() {
		// filtra os vizinhos com minas e retorna a quantidade
		return this.neighbors.stream().filter(n -> n.mined).count();
	}

	void reset() {
		this.open = false;
		this.marked = false;
		this.mined = false;
	}

	@Override
	public String toString() {
		if (this.marked) {
			return "x";
		}
		if (this.open && this.mined) {
			return "*";
		}
		if (this.open && this.nearbyMines() > 0) {
			return Long.toString(this.nearbyMines());
		}
		if (this.open) {
			return " ";
		}
		return "?";
	}

}
