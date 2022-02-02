# ScrabbleGame

Instructions to open and use this ScrabbleGame.

1. Open/expand the src folder.
2. Always open the ServerApplication first. Right click on ServerApplication
   and press run
3. If on Intelij right click PlayerApplication -> Modify Run Configuration
   -> Modify Options -> Allow Multiple Instances
4. Right click on PlayerApplication and press run. Repeat step for how many
   clients you want. (Only a maximum of 4 may play the same scrabble game and 
    a minumum of 2)
5. Input your name into the GUI and select the number of players. (Repeat for
    for every client you have. Select the same ammount of players for both clients)
6. Then have fun and play.

- Commands:
- MAKEMOVE-WORD-ROOT_COORDINATE-V/H-LETTERS (ex: MAKEMOVE-WORD-H11-V-TAG)
- MAKEMOVE-SWAP-LETTERS (ex MAKEMOVE-SWAP-ALT )
- If you come across the operator ! it's a blanck tile that can take the form 
  of any letter. USE LOWERCASE LETTER WHEN YOU WANT TO USE IT
- MAKEMOVE-WORD-H11-V-TaG (! will become a)

Rules:
- Always start with a word that will cover the center tile.