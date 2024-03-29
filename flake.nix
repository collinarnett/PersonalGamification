{
  description = "A todo list with DnD style gamification";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    sbt.url = "github:zaninime/sbt-derivation";
  };
  outputs = {
    self,
    nixpkgs,
    flake-utils,
    sbt,
  }: let
    system = "x86_64-linux";
    pkgs = nixpkgs.legacyPackages.${system};
    packageName = "PersonalGamification";
    dockerRepository = "collinarnett";
    version = "0.0.1";
    build = sbt.mkSbtDerivation.${system} {
      pname = "${packageName}";
      version = "${version}";
      depsSha256 = "sha256-ZMqRgQueCFwqieLrJw9y8pWYDxKNHTK23vlYJUGT+RQ=";
      src = ./.;
      buildPhase = ''
        sbt assembly
      '';
      installPhase = ''
        mkdir -p $out/bin
        cp target/scala-*/*-assembly-*.jar $out/bin/
      '';
    };
    openjdk = pkgs.dockerTools.pullImage {
      imageName = "openjdk";
      imageDigest = "sha256:afbe5f6d76c1eedbbd2f689c18c1984fd67121b369fc0fbd51c510caf4f9544f";
      sha256 = "sha256-VVsu0mFM1mDu1P+osJC7Inpm1FIZb5o7Q7idp5ogRP0=";
    };
  in {
    defaultPackage.${system} = build;
    packages.${system} = {
      dockerImage = pkgs.dockerTools.buildLayeredImage {
        name = "${dockerRepository}/${packageName}";
        fromImage = openjdk;
        extraCommands = ''
          mkdir -p var/lib/pg
        '';
        config.Entrypoint = [
          "/bin/java"
          "-jar"
          "${build}/bin/${packageName}-assembly-${version}.jar"
        ];
      };
      ${packageName} = build;
    };
    devShells.${system}.default = import ./shell.nix {inherit pkgs;};
  };
}
