let remoteGame = null;
let localGame = null;

function startGame() {
  let seconds = 4; // Number of seconds + 1 to wait
  
  // Start a countdown timer
  const intervalId = setInterval(function() {
    // Subtract the number of seconds left and update UI
    seconds--;
    countdownMessage(true, seconds);
    
    if (seconds == 0) { // It's time to start the game!
      clearInterval(intervalId); // Stop the countdown
      countdownMessage(false, 0); // Hide the countdown message
      
      if (remoteGame != null && localGame != null) {
        localGame.restart(); // A game already exists, lets just reset the game.
      } else {
        // Start the game managers
        remoteGame.setup();
        localGame.setup();
      }
      
    }
  }, 1000);
}

function messageVisibility(className, show) {
  var messageContainer = document.querySelector(className);
  messageContainer.style.display = show ? 'block' : 'none';
}

function waitingPlayerTwo(show) {
  messageVisibility('.waiting-message', show)
}

function gameFull() {
  messageVisibility('.game-full', true);
}

function countdownMessage(show, number) {
  messageVisibility('.countdown-message', show);
  
  const countdownNumber = document.querySelector('.countdown-number');
  countdownNumber.textContent = number;
}


// Wait till the browser is ready to render the game (avoids glitches)
window.requestAnimationFrame(function () {
  const socket = io.connect(window.location.origin);
  
  remoteGame = new GameManager(socket, true, 4, KeyboardInputManager, HTMLActuator, LocalStorageManager);
  localGame = new GameManager(socket, false, 4, KeyboardInputManager, HTMLActuator, LocalStorageManager);
  
  // Add this socket lisetner
  socket.on('player-number', function (playerNumber) {
    if (playerNumber == 1) {
      waitingPlayerTwo(true); // Show waiting message
      
      // On 2nd player connect, start the game
      socket.on('player-connect', function() {
        waitingPlayerTwo(false); // Hide waiting message
        startGame();
      });
    } else if (playerNumber === -1) {
      gameFull(); 
    } else { // Immediately start the game if we're player two
      startGame();
    }
  });
});
