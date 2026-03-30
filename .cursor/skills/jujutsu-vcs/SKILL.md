---
name: jujutsu-vcs
description: >-
  When the user or task involves commits, branches, history, merge/rebase, diffs, or "use git"
  in the WhiteLabelApp repository — use Jujutsu (jj) per project conventions and this skill.
---

# Jujutsu VCS (WhiteLabelApp)

## Defaults

1. Prefer **`jj`** for local version control (not `git commit` / `git merge` as the default path).
2. Prefer **`jj git fetch`** / **`jj git push`** for Git remotes.
3. CI lives under **`.github/workflows/`**; that is still Git/GitHub.

## Quick commands

| Task | Command |
|------|---------|
| Status | `jj status` |
| Diff | `jj diff` |
| Log | `jj log` |
| New change | `jj new` |
| Message / metadata | `jj describe` |
| Record edits | `jj commit` |
| Fetch / push | `jj git fetch`, `jj git push` |

## Setup hints

- **Clone with jj:** `jj git clone <url> [dir]`
- **Existing `git clone`:** from repo root, `jj git init --git-repo=.`

## Full detail

See [docs/jj-version-control.md](../../../docs/jj-version-control.md) and [Jujutsu documentation](https://docs.jj-vcs.dev/latest/).
