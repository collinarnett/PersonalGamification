{
  description = "A todo list with DnD style gamification";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    sbt-derivation.url = "github:zaninime/sbt-derivation";
  };
  outputs = { self, nixpkgs, flake-utils, sbt-derivation }@inputs:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ sbt-derivation.overlay ];
        };
        packageName = "PersonalGamification";
        version = "0.1.0";
      in {
        packages.${packageName} = pkgs.sbt.mkDerivation {
          pname = "${packageName}";
          version = "${version}";
          depsSha256 = "pp85kq80/cq03+huBjURytxVw9HPoj4x3UK1Nb8dnoA=";
          src = ./.;
          buildPhase = ''
            sbt assembly
          '';

          installPhase = ''
            cp target/scala-*/*-assembly-*.jar $out
          '';
        };
        devShell =
          pkgs.mkShell { buildInputs = with pkgs; [ sbt jdk scalafmt ]; };
        defaultPackage = self.packages.${system}.${packageName};
      });
}
