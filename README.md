# TCP filoverførsel

## Gruppe
Gruppe navn: Null & Void

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

#### Hvad foreslog AI?

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
  
#### Hvad ændrede I selv i planen?

Copilot manglede et trin for at opsætte de nødvendige klasser så vi tilføjede den

1. **Tilføj de nødvendige klasser**
   - Opret `FileServer.java` og `FileClient.java`.
   - Sikr at begge klasser har de grundlæggende imports og en `main`-metode, så de kan startes og testes hver for sig.

### Implementering
#### Hvor hjalp Agent jer mest?

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

#### Vigtigste feedback fra den anden gruppe:
Det vi tænkte var vigtigt fra reviewet var disse 4 punkter:
1. Når man downloader en fil hos jer, lander den direkte i root (altså roden at eres projekt mappe). Det virker helt fint her, men i et professionelt miljø ville man nok hellere se f.eks. en dedikeret ”downloads” mappe.
2. Struktur wise: kan jeres FileServer godt lige gå an. Dog kan vi se i FileClient, at i har skrevet det hele i main, der blander opsætning med forretningslogik, fejlhåndtering, læsning og udskrivning af filen. Vi har hos os f.eks. sendRequest() og saveReceivedFile().
3. Desuden så skjuler PrintWriter IO-fejl. Her kunne i passende overveje out.checkError(), eller OutputStreamWriter med eksplicit flush.
4. Kan også se jeres logik i programmet virker, men når det køre kan man kun se hvad der foregår i jeres klient, mens serveren står tomt, overvej feks. System.out.println(”Server startet på port: ” + PORT), så man også kan se der sker noget i serveren.


#### Hvad ændrede vi efter reviewet?
Vi fik tilføjet de fleste punkter, såsom output fra serverens side, downloadede filer i sin egen mappe og nogenlunde fordeling i strukturen i Client.


#### Hvad valgte vi eventuelt ikke at ændre – og hvorfor?
Det vi ikke fik implementeret fra reviewets side af, var punkt 3, PrintWriter IO-fejl. Vi var lidt i tvivl om hvad dette punkt rent faktisk betød og derfor lod den stå som den var.

## Refleksion

1. Hvor var AI mest nyttig?
   ....* Generelt var AI god til at hjælpe med at lave en plan for hvordan man skulle tilgå opgaven
   ....* AI var også god til at kunne få implementeret de forskellige trin hurtigt, hvis den havde de rigtige oplysninger fra starten af.
2. Hvornår skulle I være kritiske over for AI?
   ....* Hver gang den havde udført et trin i implementeringsplanen. Vi skulle se igennem hvilken kode den havde tilføjet og se om det var det rigtige og nødvendige der skulle bruges til de enkelte trin.
3. Hvordan kontrollerede I, at AI-genereret kode faktisk virkede?
   ....* Ved at vi fik AI'en til at teste det selv og give os et kort resume af hvad koden skulle gøre, samt selv teste det for at sikre at koden virkede som den skulle. Vi udførte selv de nødvendige tests for at sikre at den havde fulgt de stillede krav og ændrede det til hvis der var noget der manglede i koden.
   
