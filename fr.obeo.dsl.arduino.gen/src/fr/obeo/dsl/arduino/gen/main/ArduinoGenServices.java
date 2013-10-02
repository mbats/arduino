package fr.obeo.dsl.arduino.gen.main;

import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Repeat;
import fr.obeo.dsl.arduino.Sketch;

public class ArduinoGenServices {

	public int getRepeatInstructionIndex(Repeat repeat) {
		Sketch sketch = (Sketch) repeat.eContainer();
		for (int i = 0; i < sketch.getInstructions().size(); i++) {
			Instruction instruction = sketch.getInstructions().get(i);
			if (instruction instanceof Repeat && instruction.equals(repeat)) {
				return i;
			}
		}

		return 0;
	}
}
