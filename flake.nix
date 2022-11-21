{
  description = "A todo list with DnD style gamification";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    sbt-derivation.url = "github:zaninime/sbt-derivation";
  };
  outputs = { self, nixpkgs, flake-utils, sbt-derivation }@inputs:
    flake-utils.lib.eachDefaultSystem (system:
    let
      pkgs = nixpkgs.legacyPackages.${system};
        packageName = "PersonalGamification";
        dockerRepository = "collinarnett";
        version = "0.0.1";
        build = sbt-derivation.mkSbtDerivation {
          pname = "${packageName}";
          version = "${version}";
          depsSha256 = "sha256-xB/bRnwusQ1XJ/dxmKPUXRXw0Hb16l2gquFphhNN1Fc=";
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
          imageDigest =
            "sha256:afbe5f6d76c1eedbbd2f689c18c1984fd67121b369fc0fbd51c510caf4f9544f";
          sha256 = "sha256-VVsu0mFM1mDu1P+osJC7Inpm1FIZb5o7Q7idp5ogRP0=";
        };
      in {
        packages.${packageName} = build;

        defaultPackage = self.packages.${system}.${packageName};
        packages.dockerImage = pkgs.dockerTools.buildLayeredImage {
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
        devShell = import ./shell.nix { inherit pkgs; };
      });
}
