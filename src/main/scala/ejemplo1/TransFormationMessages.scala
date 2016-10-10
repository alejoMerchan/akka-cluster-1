package ejemplo1

/**
 * Created by abelmeos on 2016/10/07.
 */

  final case class TransformationJob(text: String)
  final case class TransformationResult(text: String)
  final case class JobFailed(reason: String, job: TransformationJob)
  case object BackendRegistration

