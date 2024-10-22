package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import snakes.SnakesAndLadders

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SnakesAndLaddersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val controller = SnakesAndLadders.controller;

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def createGame(size: Int) = Action { implicit request: Request[AnyContent] =>
    controller.createGame(size)
    Ok(controller.toString())
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def saveGame() = Action { implicit request: Request[AnyContent] =>
    controller.saveGame()
    Ok(controller.toString())

  }

  def loadGame() = Action { implicit request: Request[AnyContent] =>
    controller.loadGame()
    Ok(controller.toString())
  }

  def startGame() = Action { implicit request: Request[AnyContent] =>
    controller.startGame()
    Ok(controller.toString())
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def restartGame() = Action { implicit request: Request[AnyContent] =>
    controller.restartGame()
    Ok(controller.toString())
  }

  def addPlayer(name: String) = Action { implicit request: Request[AnyContent] =>
    controller.addPlayer(name)
    Ok(controller.toString())
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def rollDice() = Action { implicit request: Request[AnyContent] =>
    controller.rollDice()
    Ok(controller.toString())
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def undoLastStep() = Action { implicit request: Request[AnyContent] =>
    controller.undoLastAction()
    Ok(controller.toString())
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def gameBoard() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.game.game(controller.getCurrentGameState))
  }
}
