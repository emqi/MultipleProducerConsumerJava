public class ProdKons {
	
	static int liczbaRund=10; 
	
	public static void main(String args[]) {
		Pojemnik pojemnik = new Pojemnik(10);
		Producent prod1 = new Producent(pojemnik, "Producent 1");
		Konsument kons1 = new Konsument(pojemnik, "Konsument 1");
		Konsument kons2 = new Konsument(pojemnik, "Konsument 2");
		Konsument kons3 = new Konsument(pojemnik, "Konsument 3");
		Thread w1 = new Thread(prod1);
		Thread w2 = new Thread(kons1);
		Thread w3 = new Thread(kons2);
		Thread w4 = new Thread(kons3);
		w1.start();
		w2.start();
		w3.start();
		w4.start();
	}
	
}

class Pojemnik{
	int pojemnosc;
	int liczbaProduktow=0;
	Pojemnik(int pojemnosc) {
		this.pojemnosc=pojemnosc;
	}
	public void produkuj() {
		liczbaProduktow++;
	}
	public void konsumuj() {
		liczbaProduktow--;
	}
}

class Producent implements Runnable {
	Pojemnik p;
	String imie;
	Producent(Pojemnik p, String imie){
		this.p=p;
		this.imie=imie;
	}
	@Override
	public void run() {
		for (int i=0; i<ProdKons.liczbaRund; i++) {
			try {
				Thread.sleep((int)(100*Math.random()));
			}catch (InterruptedException e) {}
			synchronized (p) {
				while (p.liczbaProduktow == p.pojemnosc) {
					try {
						System.out.println("Bufor jest pelen. " + imie + " czeka na konsumpcje przez konsumenta.");
						p.wait();
					}catch (InterruptedException e) {}
				}
				p.produkuj();
				System.out.println(imie + " wyprodukowal 1 produkt.");
				p.notify();
			}
		}
	}
}

class Konsument implements Runnable {
	Pojemnik p;
	String imie;
	Konsument(Pojemnik p, String imie){
		this.p=p;
		this.imie=imie;
	}
	@Override
	public void run() {
		for (int i=0; i<ProdKons.liczbaRund; i++) {
			try {
				Thread.sleep((int)(100*Math.random()));
			}catch (InterruptedException e) {}
			synchronized (p) {
				while (p.liczbaProduktow==0) {
					System.out.println("Bufor jest pusty. " + imie + " czeka na produkcje przez producenta.");
					try {
						p.wait();
					}catch (InterruptedException e) {}
				}
				p.konsumuj();
				System.out.println(imie + " skonsumowal 1 produkt.");
				p.notify();
			}
		}
	}
}