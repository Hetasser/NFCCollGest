# NFCCollGest

My ultimate goal is to be able to easily manage my boardgame collection when I have to take some games with me
I have NFC tags and a NFC writer, and my phone has a NFC reader so the solution is

 - write a unique UUID to the NFC tag, and associate it with the game
 - the application has 3 screens :
  - a default scree that lists all games
  
  - an "Add item" screen that allows me to insert new games into the database with the following fields : 
      - game UUID
      - game Name
      - game min number of players
      - game max number of players
      - game duration
      - game types (list)
      
 - A modification screen with 3 actions : 
  - checkout : with a Text Field to specify the destination, be it a game night or lending it to a friend
  - checkin : removes the value of the checkout destination from the DB
  - last played : to keep track of which games have not been played for a while, updates the corresponding field in the DB
  
  
  At this point the third screen is operational, and the second screen should be. I still need to  scan the NFC before adding a game, as it will determine the UUID
  
  todo : 
   - create the first screen (listing all games with if possible filters and sorting)
   - when everything is in working order, maybe add a screen to write infos to the NFC Tag directly from the phone
   
   Help is, of course, always welcome
   As I'm not a professional developper, this is more of a hobby, there are probably a LOT of horrible things in my code, I'm always open to improvements.
