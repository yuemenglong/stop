package com.cchtrip.stop.controller.teacher

import com.cchtrip.stop.bean.{Dao, IdGenerator}
import com.cchtrip.stop.entity._
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.interfaces.Entity
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/teacher/study-job"), produces = Array("application/json"))
class StudyJobCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def postStudyJob(@RequestBody body: String): String = dao.beginTransaction(session => {
    val job = JSON.parse(body, classOf[StudyJob])
    job.id = IdGenerator.generateId
    job.crTime = new Date
    // 0. 先保存job
    session.execute(Orm.insert(job))

    // 1. 快照Course下的coursewares, videos, questions
    def getItemIds[T](clazz: Class[T], ty: String) = {
      val root = Orm.root(clazz)
      session.query(Orm.selectFrom(root).where(root.get("courseId").eql(job.courseId))).foreach(println)
      val ids = session.query(Orm.selectFrom(root).where(root.get("courseId").eql(job.courseId)))
        .map(_.asInstanceOf[Entity].$$core().fieldMap(s"${ty}Id").asInstanceOf[Long])
      ids.map(id => {
        val rel = new StudentStudyJobItem
        rel.id = IdGenerator.generateId
        rel.crTime = new Date
        rel.status = "waiting"
        rel.targetId = id
        rel.ty = ty
        rel
      })
    }

    val snapJobItems = getItemIds(classOf[CourseCourseware], "courseware") ++
      getItemIds(classOf[CourseVideo], "video") ++
      getItemIds(classOf[CourseQuestion], "question")

    // 2.每个学生一份
    val studentIds = {
      val root = Orm.root(classOf[Student])
      root.fields()
      session.query(Orm.selectFrom(root).where(root.get("clazzId").eql(job.clazzId)))
        .map(_.id)
    }

    val studentJobs = studentIds.map(id => {
      val sj = new StudentStudyJob
      sj.id = IdGenerator.generateId
      sj.crTime = new Date
      sj.status = "waiting"
      sj.studentId = id
      sj.jobId = job.id
      Orm.convert(sj)
    })
    session.execute(Orm.insert(studentJobs))
    val jobItems = studentJobs.flatMap(sj => {
      snapJobItems.map(jobItem => {
        val ret = new StudentStudyJobItem
        ret.id = IdGenerator.generateId
        ret.crTime = new Date
        ret.studentStudyJobId = sj.id
        ret.targetId = jobItem.targetId
        ret.status = jobItem.status
        ret.ty = jobItem.ty
        Orm.convert(ret)
      })
    })
    session.execute(Orm.insert(classOf[StudentStudyJobItem]).values(jobItems))
    JSON.stringifyJs(job)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteStudyJob(@PathVariable id: Long): String = dao.beginTransaction(session => {
    //1. 删除job
    //2. 删除studentJob
    //3. 删除jobItem
    val root = Orm.root(classOf[StudyJob])
    val ex = Orm.delete(root, root.leftJoin("jobs"),
      root.leftJoin("jobs").leftJoinAs("id", "studentStudyJobId", classOf[StudentStudyJobItem]))
      .from(root).where(root.get("id").eql(id))
    session.execute(ex)
    "{}"
  })


  @GetMapping(Array("/list"))
  def getStudyJobList(@RequestParam(defaultValue = "20") limit: Long,
                      @RequestParam(defaultValue = "0") offset: Long
                     ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudyJob])
    root.select("course")
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/count"))
  def getStudyJobCount: String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudyJob])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringifyJs(res)
  })

  @PutMapping(Array("/{id}"))
  def putStudyJob(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val job = JSON.parse(body, classOf[StudyJob])
    job.id = id
    session.execute(Orm.update(job))
    JSON.stringifyJs(job)
  })

  @GetMapping(Array("/{id}"))
  def getStudyJob(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val job = OrmTool.selectById(classOf[StudyJob], id, session, (root: Root[StudyJob]) => {
      root.select("jobs").select("student")
    })
    val jobIds = job.jobs.map(_.id)
    val items = {
      val root = Orm.root(classOf[StudentStudyJobItem])
      val query = Orm.selectFrom(root).where(root.get("studentStudyJobId").in(jobIds))
      session.query(query)
    }
    job.jobs.foreach(j => {
      j.items = Array()
    })
    items.groupBy(_.studentStudyJobId).foreach { case (sjId, arr) =>
      job.jobs.foreach(j => {
        if (j.id == sjId) {
          j.items = arr
        }
      })
    }
    JSON.stringifyJs(job)
  })
}
