package controllers

import org.apache.pekko.actor.{Actor, ActorRef, ActorSystem, Props}
import org.apache.pekko.stream.Materializer

import javax.inject.*
import play.api.*
import play.api.mvc.*

import scala.math.*
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import snakes.SnakesAndLadders
import snakes.util.Event.*
import snakes.util.{Event, Observer}

import scala.swing.Reactor

@Singleton
class SnakesAndLaddersController @Inject()(val controllerComponents: ControllerComponents)
  (implicit system: ActorSystem, mat: Materializer) extends BaseController {

  val controller = SnakesAndLadders.controller;

  def createGame(size: Int) = Action { implicit request: Request[AnyContent] =>
    controller.createGame(size)
    Ok(play.api.libs.json.Json.obj(
      "success" -> true,
      "gameSize" -> size,
      "message" -> s"Game of size $size created"
    ))
  }

  def startGame() = Action { implicit request: Request[AnyContent] =>
    controller.startGame()
    Redirect(routes.SnakesAndLaddersController.gameBoard())
  }

  def restartGame() = Action { implicit request: Request[AnyContent] =>
    controller.restartGame()
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

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("Connect received")
      SnakesAndLaddersSocketActorFactory.create(out)
    }
  }

  object SnakesAndLaddersSocketActorFactory {
    def create(out: ActorRef) = {
      Props(new SnakesAndLaddersSocketActor(out))
    }
  }

  class SnakesAndLaddersSocketActor(out: ActorRef) extends Actor with Reactor {
    listenTo(controller)

    def receive = {
      case msg: String =>
        println(s"Received message: $msg")
    }

    reactions += {
      case Create =>
        println("Reacting to Create event")
        out ! "Create event triggered"
      case AddPlayer =>
        println("Reacting to AddPlayer event")
        out ! "AddPlayer event triggered"
      case Roll(rollResult) =>
        println(s"Reacting to Roll event with result: $rollResult")
        out ! s"Roll event triggered: $rollResult"
      case Undo =>
        println("Reacting to Undo event")
        out ! "Undo event triggered"
      case Start =>
        println("Reacting to Start event")
        out ! "Start event triggered"
      case Load =>
        println("Reacting to Load event")
        out ! "Load event triggered"
      case Save =>
        println("Reacting to Save event")
        out ! "Save event triggered"
      case Update =>
        println("Reacting to Update event")
        out ! "Update event triggered"
      case Restart =>
        println("Reacting to Restart event")
        out ! "Restart event triggered"
    }
  }
}



