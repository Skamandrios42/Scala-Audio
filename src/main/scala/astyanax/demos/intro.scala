package astyanax.demos

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

def show(text: String) = 
    text.stripMargin.foreach { c =>
        if c == '\n' 
        then Thread.sleep((math.random() * 1000 + 20).toLong)
        else Thread.sleep((math.random() * 100 + 20).toLong)
        print(c)
    }

def waitSomeTime(x: Int, dots: Int): Unit =
    if x > 0 then
        print("\r" + "." * dots + " " * (4 - dots))
        Thread.sleep(300)
        waitSomeTime(x - 1, (dots + 1) % 4)
    else 
        print("\r   ")
        print("\r")

@main def intro =
    
    val song = Future { invincible(debugging = false) }

    waitSomeTime(12, 1)
    Thread.sleep(300)

    show {
    """|  Hallo Felix,
       |
       |Ich wünsche dir frohe Weihnachten!
       |
       |Du findest unter https://github.com/Skamandrios42/scala-audio das Repository des Synthesizer-Projekts.
       |Du kannst es dir einfach herunterladen oder es in VSCode über GIT clonen. 
       |Dazu gibt es hier mehr Infos: https://code.visualstudio.com/docs/sourcecontrol/overview#_cloning-a-repository.
       |Das Programm befindet sich im music2 package, zwei Beispiele sind im demos package.
       |In der README.md werden auch irgendwann mehr Infos zum Projekt stehen...
       |
       |Ich wünsche dir viel Spass beim experimentieren :)
       |
       |  Dein Theo         .
       |                    |
       |                 . ##
       |                 |#### .
       |                 #  ###|
       |                ##  ####
       |                 #####   .
       |               ####.#### |
       |          .   ###  |  ####   .
       |          | ###  ####  ##### |
       |          ###   ## . ##### ####
       |              ###  | ### ##       .
       |      .    ###.##  ###. ########  |    . 
       |   .  |  ###  |   ##  |      #######   |
       |   | ######   #########  . ###   ########
       |   ####       #### @$@   |  ######
       | ###   ############# $ ###################
       |                $$@$$$
       |     ____.___._.$@$$@$_.___.__.___
       |  _.___.__.___._$@$@$$___.___.__.____.___
       |       ______._______._.____.___
"""
    }

    Await.result(song, 5.minutes)

