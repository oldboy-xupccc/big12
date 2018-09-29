import scala.beans.BeanProperty

/**
  * Created by Administrator on 2018/9/29.
  */
class Dog {

    private var age = 0
    private var name = "tom"
    var color = "white"

    //生成标准get/set方法
    @BeanProperty
    var blood = "A"

    def increment = {
        age +=1
    }
    def current() = age

    def getAge() = {
        age
    }
    def setAge(age:Int) = {
        this.age =age
    }


}
