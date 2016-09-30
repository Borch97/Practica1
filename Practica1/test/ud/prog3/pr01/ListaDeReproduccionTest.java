package ud.prog3.pr01;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListaDeReproduccionTest {

	private ListaDeReproduccion lr1;
	
	private ListaDeReproduccion lr2;
	
	private final File FIC_TEST1 = new File("test/res/No del grupo.mp4");
	
	private final File FIC_TEST2 = new File("test/res/Fichero erroneo Pentatonix.mp4");
	
	@Before
	public void SetUp() throws Exception{
		lr1 = new ListaDeReproduccion();
		lr2 = new ListaDeReproduccion();
		lr2.add(FIC_TEST1);
		lr2.add(FIC_TEST2);
		
	}
	@After
	public void TearDown(){
		lr2.clear();
	}
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_Exc1() {
		lr1.getFic(0);
	}
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_Exc2(){
		lr2.getFic(-1);
	}
	@Test public void GetTest(){
		assertEquals(FIC_TEST1, lr2.getFic(0));
	}
	@Test
	public void GetTestChange(){
		lr2.intercambia(0, 1);
		assertEquals(FIC_TEST1, lr2.getFic(1));
		assertEquals(FIC_TEST2, lr2.getFic(0));
	}
	@Test
	public void GetTestAdd(){
		assertEquals(FIC_TEST1, lr2.getFic(0));
	}
	@Test
	public void GetTestRemove(){
		lr2.removeFic(1);
		assertEquals(IndexOutOfBoundsException.class, lr2.getFic(1));
	}
	@Test
	public void GetTestSize(){
		assertSame(2, lr2.size());
	}
	@Test
	public void addCarpeta(){
		String carpetaTest = "test/res/";
		String filtroTest = "*Pentatonix*.mp4";
		ListaDeReproduccion lr = new ListaDeReproduccion();
		assertEquals(2,lr.add(carpetaTest, filtroTest));
		
		//fail("Metodo sin acabar");
	}
}
