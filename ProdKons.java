import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ProdKons {
	
	public static void main(String args[]) {
		Pojemnik pojemnik1 = new Pojemnik(10, "Pojemnik A");
		Pojemnik pojemnik2 = new Pojemnik(10, "Pojemnik B");
		List<Pojemnik> lista = Collections.synchronizedList(new ArrayList<Pojemnik>());
		lista.add(pojemnik1);
		lista.add(pojemnik2);
		Producent prod1 = new Producent(lista, "Producent 1", 0);
		Producent prod2 = new Producent(lista, "Producent 2", 1);
		Konsument kons1 = new Konsument(lista, "Konsument 1", 0, 1);
		Konsument kons2 = new Konsument(lista, "Konsument 2", 0, 1);
		Konsument kons3 = new Konsument(lista, "Konsument 3", 0, 1);
		Thread w1 = new Thread(prod1);
		Thread w2 = new Thread(prod2);
		Thread w3 = new Thread(kons1);
		Thread w4 = new Thread(kons2);
		Thread w5 = new Thread(kons3);
		w1.start();
		w2.start();
		w3.start();
		w4.start();
		w5.start();
	}
	
}

class Pojemnik{
	String nazwa;
	int pojemnosc;
	int liczbaProduktow=0;
	Pojemnik(int pojemnosc, String nazwa) {
		this.pojemnosc=pojemnosc;
		this.nazwa=nazwa;
	}
	public void produkuj() {
		liczbaProduktow++;
	}
	public void konsumuj() {
		liczbaProduktow--;
	}
	public String pobierzNazwe() {
		return nazwa;
	}
}

class Producent implements Runnable {
	List <Pojemnik> lista;
	String imie;
	int numerPojemnika;
	Producent(List lista, String imie, int numerPojemnika){
		this.lista=lista;
		this.imie=imie;
		this.numerPojemnika=numerPojemnika;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((int)(100*Math.random()+50));
			}catch (InterruptedException e) {}
			synchronized (lista) {
				while (lista.get(numerPojemnika).liczbaProduktow == lista.get(numerPojemnika).pojemnosc) {
					try {
						System.out.println("Bufor jest pelen. " + imie + " czeka na konsumpcje przez konsumenta.");
						lista.wait();
					}catch (InterruptedException e) {}
				}
				lista.get(numerPojemnika).produkuj();
				System.out.println(imie + " wyprodukowal 1 produkt.");
				System.out.println("Liczba produktow w " + lista.get(numerPojemnika).pobierzNazwe() + " : " + lista.get(numerPojemnika).liczbaProduktow + ".");
				lista.notify();
			}
		}
	}
}

class Konsument implements Runnable {
	List <Pojemnik> lista;
	String imie;
	int pojemnik1;
	int pojemnik2;
	Konsument(List lista, String imie, int pojemnik1, int pojemnik2){
		this.lista=lista;
		this.imie=imie;
		this.pojemnik1=pojemnik1;
		this.pojemnik2=pojemnik2;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((int)(300*Math.random()+150));
			}catch (InterruptedException e) {}
			synchronized (lista) {
				while (!(lista.get(pojemnik1).liczbaProduktow>0 && lista.get(pojemnik2).liczbaProduktow>0)) {
					System.out.println("Bufor jest pusty. " + imie + " czeka na produkcje przez producenta.");
					try {
						lista.wait();
					}catch (InterruptedException e) {}
				}
				lista.get(pojemnik1).konsumuj();
				lista.get(pojemnik2).konsumuj();
				System.out.println(imie + " skonsumowal po 1 produkcie z kazdego pojemnika.");
				System.out.println("Liczba produktow w " + lista.get(pojemnik1).pobierzNazwe() + " : " + lista.get(pojemnik1).liczbaProduktow + ".");
				System.out.println("Liczba produktow w " + lista.get(pojemnik2).pobierzNazwe() + " : " + lista.get(pojemnik2).liczbaProduktow + ".");
				lista.notify();
			}
		}
	}
}