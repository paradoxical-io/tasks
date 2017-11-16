//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package io.paradoxical.tasks

import scala.reflect.ClassTag
import scopt.{OptionDef, OptionParser}

case class TaskDefinition[T: ClassTag](
  name: String,
  description: String,
  args: (OptionParser[T] => OptionDef[_, T])*
)
