package controllers

import org.apache.pekko.actor.{Actor, ActorRef, ActorSystem, Props}
import org.apache.pekko.stream.Materializer

import javax.inject.*
import play.api.*
import play.api.mvc.*

import scala.math.*
import play.api.libs.json.{JsNull, JsObject, Json}
import play.api.libs.streams.ActorFlow
import snakes.SnakesAndLadders
import snakes.controller.ControllerInterface
import snakes.model.playerComponent.PlayerInterface
import snakes.util.Event.*
import snakes.util.{Event, Observer}

import scala.swing.Reactor

@Singleton
class SnakesAndLaddersController @Inject()(val controllerComponents: ControllerComponents)
                                          (implicit system: ActorSystem, mat: Materializer) extends BaseController {

  val controller: ControllerInterface = SnakesAndLadders.controller;

  def createGame(size: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.createGame(size)
    NoContent
  }

  def startGame: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.startGame()
    NoContent
  }

  def restartGame: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.restartGame()
    NoContent
  }

  def addPlayer(playerName: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.addPlayer(playerName)
    NoContent
  }

  def rollDice: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.rollDice()
    NoContent
  }

  def undoLastStep: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.undoLastAction()
    NoContent
  }

  def gameState: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(getGameState)
  }

  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("New connection was established")
      SnakesAndLaddersSocketActorFactory.create(out)
    }
  }

  private def playerToJson(player: PlayerInterface): JsObject = {
    if(player != null) {
      Json.obj(
        "name" -> player.getName,
        s"color" -> s"rgb(${player.getColor.getRed}, ${player.getColor.getGreen}, ${player.getColor.getBlue})",
        "position" -> player.getPosition,
        "lastRoll" -> player.getLastRoll
      )
    } else {
      Json.obj()
    }
  }

  def getGameState: JsObject = {

    val gameState = controller.getCurrentGameState
    val players = gameState.getPlayers.map(player => playerToJson(player))
    val currentPlayer = if(!gameState.isGameStarted()) JsNull else playerToJson(gameState.getCurrentPlayer())
    val snakes = gameState.getBoard.getSnakes.map(snake => Json.obj(
      "s" -> snake._1,
      "e" -> snake._2
    ))

    Json.obj(
      "gameIsRunning" -> gameState.isGameStarted(),
      "players" -> players.toList,
      "currentPlayer" -> currentPlayer,
      "board" -> Json.obj(
        "dimensions" -> gameState.getBoard.getSize,
        "snakes" -> snakes
      )
    )
  }

  private object SnakesAndLaddersSocketActorFactory {
    def create(out: ActorRef): Props = {
      Props(new SnakesAndLaddersSocketActor(out))
    }
  }

  private class SnakesAndLaddersSocketActor(out: ActorRef) extends Actor with Reactor {

    listenTo(controller)

    def receive: Receive = {
      case msg: String => println(s"Received message: $msg")
    }

    reactions += {
      case _ => out ! getGameState.toString
    }
  }
}



