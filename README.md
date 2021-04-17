# BowlingScoreCalculator
StrongMind application demo

This is a coding demonstration for StrongMind review.  This is also a great learning exercise.

Upon running the project BowlingScoreCalculator, the user is provided with short instructions and an input legend in the window frame.

I began the exercise by creating AWT elements, like the frame and layout.  I needed to familiarize myself with this as I am new to AWT.  This coerced me into the decision to build around the GUI model instead of the logic model.  This created some unncessary challenges, but was a great learning experience.  In retrospect, I should have prioritized the calculator logic.  I knew this... but here we are.

Thank you for taking the time to review this.


---
To do:
  - Include Unit Testing
  - Important logic cleanup needed in:
      BowlingCalculator:
        updateResults()
        getTotalScore()
        getScoreStep()
      CustomTextField:
        initialize()
        onTextEntered()
  - Implement CustomPanel class to behave as a BowlingFrame to offload some of the current responsibilities of CustomTextField.  CustomTextField is meant to act as a round within the BowlingFrame, typically having 2 rounds, except in frame 10 having 3 rounds
  - Restructure GUI to eliminate unnecessary element usage and tighten AWT frame layout.
