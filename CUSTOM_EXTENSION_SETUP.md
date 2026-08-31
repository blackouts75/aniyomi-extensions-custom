# Custom extension repository setup

The source tree contains the original modules under `src/all/` plus the new
`customstream` module. The new module is intentionally a compileable
boilerplate source; replace the placeholder URL and CSS selectors in
`src/all/customstream/CustomStream.kt` before publishing it.

## Publish with GitHub Actions

1. Create a **public** GitHub repository. The repository name can be anything.
2. Push this workspace to your chosen source branch. `master` is not required;
   the workflow uses `${{ github.ref_name }}` and `${{ github.repository }}`.
3. Create the deployment branch once:

   ```bash
   git checkout --orphan repo
   git rm -rf .
   touch .gitkeep
   git add .gitkeep
   git commit -m "Initialize extension repository branch"
   git push origin repo
   git checkout -
   ```

4. In **Settings → Secrets and variables → Actions**, add:
   - `SIGNING_KEY`: base64-encoded contents of the Android `.jks` keystore
   - `ALIAS`: the keystore alias
   - `KEY_STORE_PASSWORD`: the keystore password
   - `KEY_PASSWORD`: the key password
5. Push a change to the source branch. The workflow builds every module in
   `src/*/*`, collects the APKs, inspects their source metadata, and writes
   `repo/index.min.json` (and the human-readable `repo/index.json`).
6. The generated catalog is available at:

   `https://raw.githubusercontent.com/OWNER/REPOSITORY/repo/index.min.json`

   Replace `OWNER/REPOSITORY` with the repository's actual path. The same
   branch also contains the APKs and icons.

The workflow does not contain a repository-specific owner, repository name, or
source branch. It uses the GitHub Actions context for those values and skips
the `repo` branch to avoid a deployment loop.