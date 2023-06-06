package ohm.softa.a10.controller;

import ohm.softa.a10.KitchenHatchConstants;
import ohm.softa.a10.controller.actors.Cook;
import ohm.softa.a10.controller.actors.Waiter;
import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import ohm.softa.a10.util.NameGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static ohm.softa.a10.KitchenHatchConstants.*;

public class MainController implements Initializable {

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController() {
		nameGenerator = new NameGenerator();

		/*
		self-made list of orders for the kitchen hatch
		 */
		var meals = new LinkedList<Order>();
		for (int i = 0; i < 50; i++) {
			meals.add(new Order(nameGenerator.getRandomDish()));
		}

		this.kitchenHatch = new KitchenHatchImpl(KITCHEN_HATCH_SIZE, meals);
		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		/*
		create a list of all cooks and waiters and start the threads
		 */
		var personnel = new ArrayList<Runnable>(COOKS_COUNT + WAITERS_COUNT);

		for (int i = 0; i < COOKS_COUNT; i++) {
			personnel.add(new Cook(nameGenerator.generateName(), kitchenHatch, progressReporter));
		}
		for (int i = 0; i < WAITERS_COUNT; i++) {
			personnel.add(new Waiter(nameGenerator.generateName(), kitchenHatch, progressReporter));
		}

		personnel.forEach(Runnable::run);
	}
}
