package letterplot;

import java.io.IOException;

import letterplot.model.Data;
import letterplot.views.AddSymbolDialogHandler;
import letterplot.views.MainHandler;
import nostalgia.Core;

public class Main {
	
	private static Data data = new Data();
	
	private static AddSymbolDialogHandler addSymbolDialogHandler = new AddSymbolDialogHandler(data) {
		
		@Override
		protected void showMainScreen() {
			Core.setHandler(mainHandler);
		}
	};
	
	private static MainHandler mainHandler = new MainHandler(data) {
		
		@Override
		protected void showAddSymbolDialog() {
			Core.setHandler(addSymbolDialogHandler);
		}
	}; 
	
	public static void main(String[] args) throws IOException {
		if (Core.open("LetterPlot", 800, 600, 3)) {
			System.out.println("open() exited successfully");

			Core.setCursorVisibility(false);

			Core.setHandler(mainHandler);
			boolean res = Core.run();
			if (res) {
				System.out.println("run() exited successfully");
			} else {
				System.err.println("run() failed");
			}
		} else {
			System.err.println("open() failed");
		}
	}
}
