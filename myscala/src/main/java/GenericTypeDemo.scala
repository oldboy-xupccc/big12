
/**
  * 泛型类使用
  */
object GenericTypeDemo {

    def main(args: Array[String]): Unit = {

//        //定义泛型类
//        class Pair[T,S](var first:T ,var second:S)
//        //
//        val p1 = new Pair[Int,String](1,"tomas")
//        //类型推断
//        val p2 = new Pair(1,"tomas")

//        //方法定义泛型
//        def middle[T](arr:Array[T]) :T = {
//            arr(arr.length / 2)
//        }
//
//        //调用函数
//        var arr = Array(1,2,3,4)
//        println(middle[Int](arr))


        //定义上界限
//        class Pair[T <: Comparable[T]](var first:T , var second:T){
//            //获取两个属性中较大的一个
//            def max(): T ={
//                if (first.compareTo(second) > 0) first else second ;
//            }
//        }
//
//        val p1 = new Pair("100","200")
//        println(p1.max())
//
//        //scala的String就是java的String类，可以看成是java的String的别名
//        val str:String = "hello wrold"
//
//        val i:Int = 100 ;

        //考察下界
//        class Animal
//        class Dog extends Animal
//        class Jing8 extends Dog
//
//        class Pair[T](var first :T ,var second:T){
//            //R需要是T的超类
//            def replaceFirst[R >: T](newfirst:R) = {
//                new Pair(newfirst, second)
//            }
//        }
//
//        val d1 = new Dog()
//        val d2 = new Dog()
//        val p1 = new Pair[Dog](d1,d2)
//
//        //
////        val jing8 = new Jing8()
////        //需要给方法显式指定泛型
////        val p2 = p1.replaceFirst[Jing8](jing8)
////        println(p2)
//
//        //
//        val a1 = new Animal()
//        val p3 = p1.replaceFirst(a1)
//        println(p3)

        //视图界定
//        class Pair[T <% Comparable[T]](var first: T, var second: T) {
//            //获取两个属性中较大的一个
//            def max(): T ={
//                if (first.compareTo(second) > 0) first else second ;
//            }
//        }
//
//        val p1 = new Pair(100,200)
//        println(p1.max())

        //多重界定
//        class Animal
//        class Dog extends Animal
//        class Cat extends Animal
//        class JiaFeiCat extends Cat
//
//        class Jing8 extends Dog
//        class LocalJing8 extends Jing8
//
//        //
//        class Pair[T](var first:T , var second:T){
//            def replaceFirst[R >: Jing8 <: Animal ](ele:R) ={
//                new Pair(ele , second)
//            }
//        }
//
//        val a1 = new Animal
//        val d1 = new Dog
//        val d2 = new Dog
//        val jing81 = new Jing8()
//        val jing82 = new Jing8()
//        val local1 = new LocalJing8()
//        val local2 = new LocalJing8()
//
//        val c1 =new Cat()
//
//
//        val p1 = new Pair[Dog](d1,d2)
//        val pp = p1.replaceFirst[LocalJing8](local1)
//        println(pp)


        class Person
        class Student extends Person
        class Teacher extends Person

        //协变，变化方向相同
        class Pair[+T](val first: T, val second: T) {
            //
            def makeFriends(frd : Pair[Person]) = {
                println("hello world")
            }
        }

        val p1 = new Pair[Person](new Person , new Person)
        val p2 = new Pair[Student](new Student , new Student)
        p1.makeFriends(p2)





    }
}
