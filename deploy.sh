function snapshot() {
  sbt -version="${TRAVIS_BUILD_NUMBER}-SNAPSHOT" publishSigned
}

function release() {
  sbt -version=$REVISION publishSigned sonatypeRelease
}
