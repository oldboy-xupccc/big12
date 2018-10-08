import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton, JFrame}

/**
  * Created by Administrator on 2018/10/8.
  */
object SAMDemo {
    var click:Int = 0
    def main(args: Array[String]): Unit = {
        val frame = new JFrame()
        frame.setTitle("hello swing")
        frame.setBounds(50,50 , 200,100)
        frame.setLayout(null) ;

        val btn = new JButton()
        btn.setBounds( 0 , 0 , 100,50)
        btn.setText("ok")
        //常规java实现
//        btn.addActionListener(new ActionListener {
//            override def actionPerformed(e: ActionEvent) = {
//                click += 1
//                println(click)
//            }
//        })

        //定义隐式转换函数,将函数转换成actionListen对象
        implicit  def func2ActionLisnter(f:(ActionEvent)=> Unit)= {
            new ActionListener {
                override def actionPerformed(e: ActionEvent) = {
                    f(e)
                }
            }
        }

        btn.addActionListener((e: ActionEvent)=>{click +=1 ; println(click)}) ;
        frame.add(btn)

        frame.setVisible(true)
    }
}
