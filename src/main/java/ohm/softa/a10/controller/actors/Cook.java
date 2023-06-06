package ohm.softa.a10.controller.actors;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;

public class Cook implements Runnable {

	private String name;
	private KitchenHatch kitchenHatch;
	private ProgressReporter progressReporter;

	public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		while (kitchenHatch.getOrderCount() > 0) {
			var order = kitchenHatch.dequeueOrder(5_000);
			var dish = new Dish(order.getMealName());
			try {
				Thread.sleep(dish.getCookingTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			kitchenHatch.enqueueDish(dish);
			progressReporter.updateProgress();
		}
		progressReporter.notifyCookLeaving();
	}
}
