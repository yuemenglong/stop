package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
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
@RequestMapping(value = Array("/study-job"), produces = Array("application/json"))
class StudyJobCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def postStudyJob(@RequestBody body: String): String = dao.beginTransaction(session => {
    val job = JSON.parse(body, classOf[StudyJob])
    job.crTime = new Date
    // 0. 先保存job
    session.execute(Orm.insert(job))

    // 1. 快照Course下的coursewares, videos, questions
    def getIds[T](clazz: Class[T], ty: String) = {
      val root = Orm.root(clazz)
      root.fields()
      val ids = session.query(Orm.selectFrom(root).where(root.get("id").eql(job.courseId)))
        .map(_.asInstanceOf[Entity].$$core().fieldMap("id").asInstanceOf[Long])
      ids.map(id => {
        val rel = new StudentStudyJobItem
        rel.crTime = new Date
        rel.status = "waiting"
        rel.targetId = id
        rel.ty = ty
        rel
      })
    }

    val snapJobItems = getIds(classOf[Courseware], "courseware") ++
      getIds(classOf[Video], "video") ++
      getIds(classOf[Question], "question")

    // 2.每个学生一份
    val studentIds = {
      val root = Orm.root(classOf[Student])
      root.fields()
      session.query(Orm.selectFrom(root).where(root.get("clazzId").eql(job.clazzId)))
        .map(_.id)
    }

    val studentJobs = studentIds.map(id => {
      val sj = new StudentStudyJob
      sj.crTime = new Date
      sj.studentId = id
      sj.jobId = job.id
      Orm.convert(sj)
    })
    session.execute(Orm.insert(classOf[StudentStudyJob]).values(studentJobs))
    val jobItems = studentJobs.flatMap(sj => {
      snapJobItems.map(jobItem => {
        val ret = new StudentStudyJobItem
        ret.crTime = jobItem.crTime
        ret.studentStudyJobId = sj.id
        ret.targetId = jobItem.targetId
        ret.status = jobItem.status
        ret.ty = jobItem.ty
        Orm.convert(ret)
      })
    })
    session.execute(Orm.insert(classOf[StudentStudyJobItem]).values(jobItems))
    JSON.stringify(job)
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
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def getStudyJobCount: String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudyJob])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })

  @PutMapping(Array("/{id}"))
  def putStudyJob(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val job = JSON.parse(body, classOf[StudyJob])
    job.id = id
    session.execute(Orm.update(job))
    JSON.stringify(job)
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
    JSON.stringify(job)
  })
}
