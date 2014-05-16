Progetto FVA: Earify
========
#### Gruppo 2
[Vai al wiki](https://github.com/mattmezza/FVA/wiki 'Vai al wiki')
###### Matteo Merola, Carlo Branca, Simone Scalabrino

### MODULO DI ESTRAZIONE DELLE CARATTERISTICHE DELL’ORECCHIO
##### Input:
- immagine orecchio segmentata;
- username;
- punteggio qualità;
- Azione: registrazione/riconoscimento/verifica.

##### Funzionalità principali:
- Estrazione delle caratteristiche con uno o più algoritmi (definito in un file di configurazione);
- Salvataggio del vettore delle caratteristiche, con associato lo username, in caso di registrazione.

##### Output:
- Uno o più vettori delle caratteristiche (a seconda del numero di algoritmi di feature extraction usati);
- Username;
- punteggio qualità;
- Azione: registrazione /riconoscimento/verifica.

##### Note:
Occorre simulare l’input del modulo precedente: visualizzare una interfaccia  che permetta di scegliere una immagine di orecchio dal dispositivo, di inserire lo username e di specificare l’azione. Usare il valore fittizio 1 per il punteggio di qualità.
Il numero ed il tipo di algoritmi per la feature extration da applicare sull’immagine in input è definito in un file di configurazione, ad esempio:
* LBP: true;
* PCA: true;
* SIFT: false;
* ...


I vettori delle caratteristiche da restituire in output devono essere organizzati in una struttura di tipo chiave-valore (es. Hash Map: key=LBP, value=[11, 23, 55, 11, ...], key=..., value=...).
In caso di registrazione l’esecuzione termina dopo il salvataggio, altrimenti viene chiamato il modulo successivo con i parametri di output specificati sopra. Simulare chiamata al modulo successivo visualizzando gli output in una schermata.
 
##### Dettagli
Implementare un algoritmo di riconoscimento a vostra scelta ad esempio uno di quelli presentati a lezione. Il suggerimento è di implementare SIFT (specificato nelle slide “[Algoritmi_orecchio](https://www.dropbox.com/sh/5m1blyl48wdfj9j/5BFvD-WApF 'Scarica lezione')” presentate al corso e pubblicate all’indirizzo [dropbox](https://www.dropbox.com/sh/5m1blyl48wdfj9j/5BFvD-WApF 'Vai a dropbox'). In alternativa potete scegliere di implementare una versione modificata di LBP (nel caso vi verrà fornito del materiale utile) o proporre un altro algoritmo (che può essere sia uno di quelli visti a lezione sia uno trovato a seguito di vostre ricerche). In caso di algoritmo proposto da voi, questo verrà prima valutato ed eventualmente accettato.
A lezione vi verranno poi forniti gli eventuali altri algoritmi già implementati in *C* e da integrare nella app. E’ importante che l’applicazione sia comunque predisposta a contenere più algoritmi di riconoscimento e a permettere di aggiungerne altri in futuro.
