export class Category {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	level: number = undefined;
	parentId: number = undefined;
	ty: string = undefined;
	parent: Category = undefined;
	children: Category[] = [];
}

export class Clazz {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	studentCount: number = undefined;
	students: Student[] = [];
}

export class Course {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	description: string = undefined;
	difficulty: string = undefined;
	categoryId: number = undefined;
	category: Category = new Category();
	questions: Question[] = [];
	coursewares: Courseware[] = [];
	videos: Video[] = [];
	questionCount: number = undefined;
	coursewareCount: number = undefined;
	videoCount: number = undefined;
}

export class Courseware {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	courseId: number = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
}

export class Question {
	id: number = undefined;
	crTime: string = undefined;
	courseId: number = undefined;
	title: string = undefined;
	score: number = undefined;
	answer: string = undefined;
	ty: string = undefined;
	sc: QuestionChoice = new QuestionChoice();
	categoryId: number = undefined;
}

export class QuestionChoice {
	id: number = undefined;
	crTime: string = undefined;
	a: string = undefined;
	b: string = undefined;
	c: string = undefined;
	d: string = undefined;
}

export class Student {
	id: number = undefined;
	crTime: string = undefined;
	user: User = new User();
	name: string = undefined;
	mobile: string = undefined;
	password: string = undefined;
	email: string = undefined;
	avatar: string = undefined;
	clazz: Clazz = new Clazz();
	clazzId: number = undefined;
	team: TeamApply = new TeamApply();
	jobs: StudentStudyJob[] = [];
}

export class StudentStudyJob {
	id: number = undefined;
	crTime: string = undefined;
	job: StudyJob = new StudyJob();
	jobId: number = undefined;
	student: Student = new Student();
	studentId: number = undefined;
	status: string = undefined;
	items: StudentStudyJobItem[] = [];
}

export class StudentStudyJobItem {
	id: number = undefined;
	crTime: string = undefined;
	studentStudyJobId: number = undefined;
	targetId: number = undefined;
	ty: string = undefined;
	status: string = undefined;
	answer: string = undefined;
	correct: boolean = undefined;
	score: number = undefined;
}

export class StudyJob {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	course: Course = new Course();
	courseId: number = undefined;
	clazz: Clazz = new Clazz();
	clazzId: number = undefined;
	limitDate: string = undefined;
	jobs: StudentStudyJob[] = [];
}

export class Team {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	creater: Student = new Student();
	createrId: number = undefined;
	students: TeamApply[] = [];
	studentCount: number = undefined;
}

export class TeamApply {
	id: number = undefined;
	crTime: string = undefined;
	student: Student = undefined;
	studentId: number = undefined;
	team: Team = undefined;
	teamId: number = undefined;
	status: string = undefined;
}

export class User {
	id: number = undefined;
	crTime: string = undefined;
	username: string = undefined;
	password: string = undefined;
	role: string = undefined;
	ty: string = undefined;
}

export class Video {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	courseId: number = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
}