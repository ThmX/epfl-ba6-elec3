package ch.thmx.simulator;

import java.util.Observable;
import java.util.Observer;

import org.springframework.stereotype.Service;

@Service("simConsole")
public class SimConsole extends Observable implements Observer {

	@Override
	public void update(Observable obs, Object obj) {
		setChanged();
		notifyObservers(obj);
	}
	
}
