const int refreshDelay = 1;
const int cadenceValue = 2;
const int counterPin = 3;
const int cadencePin = 4;
const int stateLed = 5;

boolean isCounterHigh = false;
boolean isCadenceHigh = false;

long lastCounterTime = 0;

void setup() {
  Serial.begin(115200);
  
  pinMode(counterPin, INPUT);
  pinMode(cadencePin, INPUT);
  pinMode(stateLed, OUTPUT);
  
  lastCounterTime = millis();
}

void loop() {
  handleCounter();
  handleCadence();
  delay(refreshDelay);
}

void handleCounter() {
  int counterButtonState = digitalRead(counterPin);
  if (counterButtonState == HIGH && !isCounterHigh) {
    isCounterHigh = true;
    long diff = calculateDiff();
    Serial.write(diff);
    digitalWrite(stateLed, HIGH);
  } else if (isCounterHigh && counterButtonState == LOW) {
    isCounterHigh = false;
    digitalWrite(stateLed, LOW);
  }
}

long calculateDiff() {
  long currentTime = millis();
  long diff = currentTime - lastCounterTime;
  lastCounterTime = currentTime;
  return diff;
}

void handleCadence() {
  int cadenceButtonState = digitalRead(cadencePin);
  if (cadenceButtonState == HIGH && !isCadenceHigh) { 
    isCadenceHigh = true; 
    Serial.write(cadenceValue);
    digitalWrite(stateLed, HIGH);
  } else if (isCadenceHigh && cadenceButtonState == LOW) { 
    isCadenceHigh = false;
    digitalWrite(stateLed, LOW);  
  }
}
