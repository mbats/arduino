int brightness;

void setup() {
  brightness=0;
}

// the loop routine runs over and over again forever:
void loop() {
 ** While1 = fr.obeo.dsl.arduino.impl.WhileImpl@1fc3a11
 while (brightness<255)
 {
 toto
  brightness=(brightness+1);
  analogWrite(11,255-brightness);
  delay(5);
 }
 ** While2 = fr.obeo.dsl.arduino.impl.SetImpl@4559
 analogWrite(11,255-brightness);
 delay(5);
 ** While3 = fr.obeo.dsl.arduino.impl.LevelImpl@119bc7c
}
