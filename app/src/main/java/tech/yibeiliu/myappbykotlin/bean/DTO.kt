package tech.yibeiliu.myappbykotlin.bean

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
class Subjects(var title: String, var mainland_pubdate: String, var images: Images, var durations: List<String>)

class Images(var small: String, var large: String, var medium: String)

class Movies(var count: Int, var start: Int, var total: Int, var subjects: MutableList<Subjects>)