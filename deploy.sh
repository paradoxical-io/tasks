function snapshot() {
  sbt -Drevision="$TRAVIS_BUILD_NUMBER-SNAPSHOT" publishSigned
}

function release() {
  sbt -Drevision=$REVISION publishSigned sonatypeRelease
}
