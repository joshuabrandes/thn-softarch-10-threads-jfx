package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch {

	private  int maxMeals;
	private Deque<Order> orders;
	private Deque<Dish> dishes = new LinkedList<>();

	public KitchenHatchImpl(int maxMeals, LinkedList<Order> orders) {
		this.maxMeals = maxMeals;
		this.orders = orders;
	}

	@Override
	public int getMaxDishes() {
		return maxMeals;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		while (orders.size() == 0) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return orders.poll();
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout) {
		while (dishes.size() == 0) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return dishes.poll();
	}

	@Override
	public void enqueueDish(Dish m) {
		while (dishes.size() >= maxMeals) {
			try {
				wait(10_000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getDishesCount() {
		return dishes.size();
	}
}
