public class ProdKons {
	
	public static void main(String args[]) {
		Container container = new Container(10);
		Producent prod1 = new Producent(container, "Producent 1");
		Konsument kons1 = new Konsument(container, "Konsument 1");
		Konsument kons2 = new Konsument(container, "Konsument 2");
		Thread w1 = new Thread(prod1);
		Thread w2 = new Thread(kons1);
		Thread w3 = new Thread(kons2);
		w1.start();
		w2.start();
		w3.start();
	}
	
}

class Container{
	int capacity;
	int numberOfItems=0;
	Container(int capacity) {
		this.capacity=capacity;
	}
	public void produce() {
		numberOfItems++;
	}
	public void consume() {
		numberOfItems--;
	}
}

class Producent implements Runnable {
	Container p;
	String name;
	Producent(Container p, String name){
		this.p=p;
		this.name=name;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((int)(100*Math.random()+50));
			}catch (InterruptedException e) {}
			synchronized (p) {
				while (p.numberOfItems == p.capacity) {
					try {
						System.out.println("Bufor jest pelen. " + name + " czeka na konsumpcje przez konsumenta.");
						p.wait();
					}catch (InterruptedException e) {}
				}
				p.produce();
				System.out.println(name + " wyprodukowal 1 produkt.");
				System.out.println("Liczba produktow: " + p.numberOfItems);
				p.notify();
			}
		}
	}
}

class Konsument implements Runnable {
	Container p;
	String name;
	Konsument(Container p, String name){
		this.p=p;
		this.name=name;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((int)(200*Math.random()+75));
			}catch (InterruptedException e) {}
			synchronized (p) {
				while (p.numberOfItems==0) {
					System.out.println("Bufor jest pusty. " + name + " czeka na produkcje przez producenta.");
					try {
						p.wait();
					}catch (InterruptedException e) {}
				}
				p.consume();
				System.out.println(name + " skonsumowal 1 produkt.");
				System.out.println("Liczba produktow: " + p.numberOfItems);
				p.notify();
			}
		}
	}
}