package ohm.softa.a10.controller.actors;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;

import java.util.Random;

public class Waiter implements Runnable {

	private String name;
	private KitchenHatch kitchenHatch;
	private ProgressReporter progressReporter;

	private Random random = new Random();

	@Override
	public void run() {
		while (kitchenHatch.getDishesCount() > 0) {
			var dish = kitchenHatch.dequeueDish(5_000);
			try {
				Thread.sleep(random.nextInt(1_000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressReporter.updateProgress();
		}
		progressReporter.notifyWaiterLeaving();
	}
}
