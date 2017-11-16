function snapshot() {
  echo "SNAPSHOT"
  sbt -Drevision="${TRAVIS_BUILD_NUMBER}-SNAPSHOT" publishSigned
}

function release() {
  echo "RELEASE"
  sbt -Drevision=$REVISION publishSigned sonatypeRelease
}
