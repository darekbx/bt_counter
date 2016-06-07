# README #


```
#!c#
double tire_circumference = 2075;
double revolutions = 1;

// dystans ilosc tykniec * obwod kola
double distance = tire_circumference * revolutions;

// predkosc obwod kola dzielony przez czas miedzy tyknieciami 
double last_interval = 0.812d; // czas jednego obrotu kola
double ms = (tire_circumference / 1000) / (double)last_interval;

double kmh = ms * 60 * 60;
kmh /= 1000;
```



BTCounter TODO:


```
#!java

Trasy:
- pomiar prędkości (km/h, m/s)
- pomiar kadencji (obr/min)
- sygnalizacja odbioru danych
- dodanie trasy
- wybór trasy
- punkty trasy:
  - klatka B
  - skrzyżowanie lazurowej z człuchowską
  - skrzyżowanie człuchowskiej z powstańców
  - przejazd kolejowy
  - światła przy kasprzaka/sowińskiego
  - światła przy ordona
  - światła przy prymasa
  - światła przy płockiej
  - biuro
- ghost:
  - offset względem najlepszego czasu
  - czas na zielono jak jedziemy lepiej niż najlepszy czas
  - czas na czerwono jak jedziemy gorzej niż najlepszy czas
  - oś odcinków jak w grach rajdowych (z kolorami zielono/czerwono)
```