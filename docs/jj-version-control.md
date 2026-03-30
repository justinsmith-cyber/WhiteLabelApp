# Version control: Jujutsu (`jj`)

This project uses **[Jujutsu](https://jj-vcs.dev/)** (`jj`) for everyday version control. Collaboration and CI still go through a **Git** remote (for example GitHub); `jj` talks to that remote via `jj git …` commands.

Official reference: [Jujutsu documentation](https://docs.jj-vcs.dev/latest/) (tutorial, [Git comparison](https://docs.jj-vcs.dev/latest/git-comparison/), [command table](https://docs.jj-vcs.dev/latest/git-command-table)).

## Layout in this repo

- [`.gitignore`](../.gitignore) lists `.jj/`, so Jujutsu metadata is **not** committed when you push with Git. That is normal for a **colocated** setup: share history through Git; each machine has its own `.jj/` directory.
- [`.github/workflows/`](../.github/workflows/) defines CI. GitHub runs those workflows against Git branches; nothing here replaces that with a jj-only remote unless you deliberately change hosting.

## Install

- **macOS (Homebrew):** `brew install jj`
- **Other platforms:** see [Getting started](https://docs.jj-vcs.dev/latest/) in the jj docs.

Check the CLI: `jj version`

## Get a working copy

### Clone with jj (recommended for jj-first workflows)

Creates a Git-backed jj repo (colocated by default so `.git` and `.jj` live together):

```bash
jj git clone <repository-url> [destination-directory]
```

### Already cloned with `git`

From the repository root (where `.git` exists), attach jj to the existing Git repo:

```bash
jj git init --git-repo=.
```

Use `jj git colocation status` to confirm colocation. If someone committed only with Git while you use jj, you may need `jj git import` to align jj’s view with the Git side (see `jj git import --help`).

## Daily workflow

| Goal | Command |
|------|---------|
| Overview of working copy and parents | `jj status` (alias: `jj st`) |
| Patch-style changes | `jj diff` |
| History | `jj log` |
| New empty change on top | `jj new` |
| Edit commit message / metadata | `jj describe` |
| Record current working-copy edits into history | `jj commit` (often after `jj describe`; see `jj commit --help`) |
| Move edits between changes | `jj squash`, `jj split` |
| Drop a change | `jj abandon` |
| Undo last jj operation | `jj undo` |

Jujutsu uses **changes** and the **working-copy revision** (`@`) rather than Git’s branch-checkout model; read the [tutorial](https://docs.jj-vcs.dev/latest/tutorial/) for the mental model.

## Sync with the Git remote

| Goal | Command |
|------|---------|
| Fetch | `jj git fetch` |
| Push | `jj git push` |
| Remotes | `jj git remote` (list/add/remove) |

**Bookmarks** are the usual jj way to name heads you care about for Git interop (often mapped to branches on push). See `jj bookmark --help` and the docs section on Git compatibility.

**Export / import** (when Git and jj diverge outside normal fetch/push):

- `jj git export` — update the underlying Git repo from jj’s history
- `jj git import` — bring Git-side commits into the jj repo

## For automation and agents

- Prefer **`jj`** for local history (status, diff, log, commit, rebase, etc.) when this repository has a `.jj/` directory.
- Use **`jj git fetch`** / **`jj git push`** for remote Git operations instead of teaching raw `git pull` / `git push` as the default, unless the user is in a Git-only checkout with no jj repo.
- Reading or editing **`.github/workflows/*.yml`** is still correct for CI changes.
