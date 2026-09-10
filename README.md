# TCP filoverførsel

## Gruppe
gruppe navn: Null & Void

Navne: Mathias Lund, Henriette Larsen, Sofie Jørgensen, Oliver Ellegaard.

## Løsningen

her er det typiske flow:
1. FileClient opretter forbindelse til FileServer.
2. FileClient sender fx GET|rapport.txt.
3. FileServer læser kommandoen og splitter den ved |.
4. Serveren tjekker:
  - om kommandoen er GET
  - om filnavnet findes
  - om filen kan åbnes/læses
5. Hvis alt er OK, sender FileServer filens indhold tilbage.
6. Hvis noget fejler, sender FileServer en fejlbesked tilbage, fx ERROR|Fil findes ikke.


## AI-agent

### Plan

Hvad foreslog AI?

1. **Server starter og klient forbinder**
   - Implementér `FileServer` så den lytter på port 5000.
   - Implementér `FileClient` så den kan oprette forbindelse.
   - Test: klienten kan forbinde uden at sende data endnu.

2. **Send og læs `GET|filnavn`**
   - Klienten sender en tekstbesked.
   - Serveren læser og splitter beskeden.
   - Test: serveren kan udskrive/modtage filnavnet korrekt.

3. **Valider kommando og filnavn**
   - Tjek at kommandoen er `GET`.
   - Afvis ugyldige filnavne som `../`.
   - Test: serveren giver fejl ved ugyldige inputs.

4. **Returnér `OK` eller `ERROR|besked`**
   - Serveren sender status tilbage til klienten.
   - Test: klienten kan læse både succes og fejl som tekst.

5. **Send filen som bytes**
   - Ved `OK` sender serveren filens indhold som rå bytes.
   - Test: små filer overføres korrekt uden at blive ødelagt.

6. **Gem filen hos klienten**
   - Klienten modtager bytes og skriver dem til en lokal fil.
   - Test: den gemte fil matcher originalen.

7. **Fejlhåndtering og lukning**
   - Håndtér fil ikke fundet, forbindelsesfejl og I/O-fejl.
   - Luk sockets og streams korrekt i begge klasser.
   - Test: ressourcer lukkes også ved fejl.
  
Hvad ændrede I selv i planen?

Copilot manglede et trin for at opsætte de nødvendige klasser så vi tilføjede den

1. **Tilføj de nødvendige klasser**
   - Opret `FileServer.java` og `FileClient.java`.
   - Sikr at begge klasser har de grundlæggende imports og en `main`-metode, så de kan startes og testes hver for sig.

### Implementering
Hvor hjalp Agent jer mest?

Efter 3 iterationer hvor vi for hver gang gav den bedre krav og retningslinjer endte den med at være en god hjælp til at lave en plan samt skrive selve koden 

### Kritisk vurdering

Et AI-forslag vi fulgte:
1. **Tilføj de nødvendige klasser**
   - Opret `FileServer.java` og `FileClient.java`.
   - Sikr at begge klasser har de grundlæggende imports og en `main`-metode, så de kan startes og testes hver for sig.
   
Et AI-forslag vi ændrede eller afviste:

3. **Send og læs `GET|filnavn`**
   - Klienten sender en tekstbesked.
   - Serveren læser og splitter beskeden.
   - Test: serveren kan udskrive/modtage filnavnet korrekt.

Hvorfor?
Da Copilot implementerede dette trin så blev "GET|Text" hardcoded så der var ikke noget input fra brugeren.

## Test

| Test | Resultat |
|---|---|
| Normal fil | Filen blev overført |
| Stor fil | Hele filen blev overført  |
| Ukendt fil | Fejlbesked "Filen findes ikke"|
| ../hemmelig.txt | Fejlbesked "Ukendt fil"|
| Server ikke startet |Fejlbesked om forbindelses fejl |

## Peer review

Vigtigste feedback fra den anden gruppe:

Hvad ændrede vi efter reviewet?

Hvad valgte vi eventuelt ikke at ændre – og hvorfor?

## Refleksion

1. Hvor var AI mest nyttig?
2. Hvornår skulle I være kritiske over for AI?
3. Hvordan kontrollerede I, at AI-genereret kode faktisk virkede?
