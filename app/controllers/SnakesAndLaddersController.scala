package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.math._
import play.api.libs.json.Json

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
    Ok(play.api.libs.json.Json.obj(
      "success" -> true,
      "gameSize" -> size,
      "message" -> s"Game of size $size created"
    ))
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
    Redirect(routes.SnakesAndLaddersController.index())
  }

  def addPlayer(playerName: String) = Action { implicit request: Request[AnyContent] =>
    controller.addPlayer(playerName)
    val players = controller.getCurrentGameState.getPlayers.map(_.getName).toList
    Ok(Json.obj("players" -> players))
  }

  def rollDice() = Action { implicit request: Request[AnyContent] =>
    controller.rollDice()

    val players = controller.getCurrentGameState.getPlayers.toList
    val currentPlayer = controller.getCurrentGameState.getCurrentPlayer()
    val boardSize = sqrt(controller.getBoardSize).toInt
    val rolledValue = currentPlayer.getLastRoll

    // Render
    val boardHtml = views.html.game(boardSize, players, currentPlayer.getName, rolledValue).toString

    Ok(Json.obj(
      "boardHtml" -> boardHtml
    ))
  }

  def undoLastStep() = Action { implicit request: Request[AnyContent] =>
    controller.undoLastAction()

    val players = controller.getCurrentGameState.getPlayers.toList
    val currentPlayer = controller.getCurrentGameState.getCurrentPlayer()
    val boardSize = sqrt(controller.getBoardSize).toInt
    val rolledValue = currentPlayer.getLastRoll

    // Render
    val boardHtml = views.html.game(boardSize, players, currentPlayer.getName, rolledValue).toString

    Ok(Json.obj(
      "boardHtml" -> boardHtml
    ))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def gameBoard() = Action { implicit request: Request[AnyContent] =>
    val players = controller.getCurrentGameState.getPlayers.toList // Convert Queue to List
    val currentPlayer = controller.getCurrentGameState.getCurrentPlayer()
    val boardSize = sqrt(controller.getBoardSize).toInt
    val rolledValue = controller.getCurrentGameState.getCurrentPlayer().getLastRoll
    Ok(views.html.game(boardSize, players, currentPlayer.getName, rolledValue))
  }
}
