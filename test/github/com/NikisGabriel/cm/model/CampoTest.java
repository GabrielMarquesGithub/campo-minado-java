package github.com.NikisGabriel.cm.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import github.com.NikisGabriel.cm.exeption.ExplosionException;

class CampoTest {

	private Campo campo;

	// basicamente com a utilização do @BeforeEach esse método vai ser executado a
	// cada teste realizado resetando os valores para um padrão assim ao final de
	// cada test
	@BeforeEach
	void iniciaCampo() {
		this.campo = new Campo(3, 3);
	}

	@Test
	void testeVizinhoDistancia1() {
		Campo vizinho = new Campo(3, 2);
		boolean res = this.campo.addNeighbors(vizinho);
		assertTrue(res);
	}

	@Test
	void testeVizinhoDistancia2() {
		Campo vizinho = new Campo(2, 2);
		boolean res = this.campo.addNeighbors(vizinho);
		assertTrue(res);
	}

	@Test
	void testeVizinhoFalso() {
		Campo vizinho = new Campo(3, 5);
		boolean res = this.campo.addNeighbors(vizinho);
		assertFalse(res);
	}

	@Test
	void testeVizinhoFalsoDiagonal() {
		Campo vizinho = new Campo(1, 2);
		boolean res = this.campo.addNeighbors(vizinho);
		assertFalse(res);
	}

	@Test
	void testeComeçaDesmarcado() {
		assertFalse(this.campo.isMarked());
	}

	@Test
	void testeAlternarMarcação() {
		this.campo.toggleChecked();
		assertTrue(this.campo.isMarked());
	}

	@Test
	void testeAlternarMarcaçãoDuasChamadas() {
		this.campo.toggleChecked();
		this.campo.toggleChecked();
		assertFalse(this.campo.isMarked());
	}

	@Test
	void testeAlternarMarcaçãoCampoAberto() {
		this.campo.openCampo();
		this.campo.toggleChecked();
		assertFalse(this.campo.isMarked());
	}

	@Test
	void testeVizinhancaSegura() {
		Campo vizinho = new Campo(3, 2);
		this.campo.addNeighbors(vizinho);
		boolean res = this.campo.safeNeighborhood();
		assertTrue(res);
	}

	@Test
	void testeAbrirCampoNaoMarcadoNaoAbertoNaoMinado() {
		boolean res = this.campo.openCampo();
		assertTrue(res);
	}

	@Test
	void testeAbrirCampoMarcado() {
		this.campo.toggleChecked();
		boolean res = this.campo.openCampo();
		assertFalse(res);
	}

	@Test
	void testeAbrirCampoJaAberto() {
		this.campo.openCampo();
		boolean res = this.campo.openCampo();
		assertFalse(res);
	}

	@Test
	void testeAbrirCampoMinado() {
		this.campo.undermine();
		// assert para lançamento de exceções, recebe uma exceção.class e uma função
		// lambda anônima que executa algo que deve retornar a exceção recebida
		assertThrows(ExplosionException.class, () -> {
			this.campo.openCampo();
		});
	}

	@Test
	void testeAbrirCampoComVizinhosSeguros() {
		Campo vizinho = new Campo(3, 2);
		this.campo.addNeighbors(vizinho);
		boolean res = this.campo.openCampo();
		assertTrue(res);
	}

	@Test
	void testeAbrirCampoComVizinhosNaoSeguros() {
		Campo vizinho = new Campo(3, 2);
		vizinho.undermine();
		this.campo.addNeighbors(vizinho);
		boolean res = this.campo.openCampo();
		assertTrue(res);
	}

	@Test
	void testeAbrirCamposVizinhosSegurosEmSucessão() {
		Campo vizinho = new Campo(3, 2);
		Campo vizinhoDoVizinho = new Campo(3, 1);

		vizinho.addNeighbors(vizinhoDoVizinho);
		this.campo.addNeighbors(vizinho);

		this.campo.openCampo();

		assertTrue(vizinho.isOpen() && vizinhoDoVizinho.isOpen());
	}

	@Test
	void testeAbrirCamposVizinhosComMinasEmSucessão() {
		Campo vizinho = new Campo(3, 2);
		Campo vizinhoDoVizinho = new Campo(3, 1);

		vizinho.addNeighbors(vizinhoDoVizinho);
		this.campo.addNeighbors(vizinho);

		// colocando mina no campo mais afastado
		vizinhoDoVizinho.undermine();

		this.campo.openCampo();

		assertTrue(vizinho.isOpen() && vizinhoDoVizinho.isClose());
	}

	@Test
	void testeEstaFechado() {
		this.campo.openCampo();
		assertFalse(this.campo.isClose());
	}

	@Test
	void testeGetLinha() {
		assertEquals(3, this.campo.getRow());
	}

	@Test
	void testeGetColuna() {
		assertEquals(3, this.campo.getColumn());
	}

	@Test
	void testeCampoNaoResolvido() {
		assertFalse(this.campo.concluded());
	}

	@Test
	void testeCampoConcluidoSemMina() {
		this.campo.openCampo();
		assertTrue(this.campo.concluded());
	}

	@Test
	void testeCampoConcluidoComMina() {
		this.campo.undermine();
		this.campo.toggleChecked();
		assertTrue(this.campo.concluded());
	}

	@Test
	void testeMinasProximas() {
		Campo vizinho1 = new Campo(3, 2);
		Campo vizinho2 = new Campo(2, 2);

		vizinho1.undermine();
		vizinho2.undermine();

		this.campo.addNeighbors(vizinho1);
		this.campo.addNeighbors(vizinho2);

		assertEquals(2, this.campo.nearbyMines());
	}

	@Test
	void testeCampoResetado() {
		this.campo.toggleChecked();
		assertTrue(this.campo.isMarked());

		this.campo.reset();
		assertFalse(this.campo.isMarked());
	}

	@Test
	void testeDeImpressãoCampoPadrão() {
		assertEquals("?", this.campo.toString());
	}

	@Test
	void testeDeImpressãoCampoMarcado() {
		this.campo.toggleChecked();
		assertEquals("x", this.campo.toString());
	}

}
