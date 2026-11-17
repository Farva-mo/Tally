# Tally — Remaining Work (Checkpoint #5 handoff)

## Already done

- All 11 screens exist with both portrait and landscape layouts (rotation handled).
- Working local auth: sign-in and create-account validate input and persist accounts via
  `AuthStore` (SharedPreferences, no backend). Demo login: `demo@tally.app` / `tally1234`
  (auto-seeded on first launch).
- Input validation + Toast/Snackbar feedback wired up on sign-in, create-account, new-group,
  and add-expense.
- Validation-error state (red field errors, warning banners) now survives device rotation on
  those same four screens.
- Unused adapter/model classes (`GroupsAdapter`, `MemberBalanceAdapter`, `FeedAdapter`, and
  their model classes) removed — they were built but never wired to any screen.

## What's left — priority order

### 1. Persist groups and expenses (the big one)

Only login persists right now. This blocks the checkpoint requirement that data survive
closing and relaunching the app.

- `NewGroupActivity.java` generates a join code but never saves the typed group name anywhere.
- `AddExpenseActivity.java` now validates the amount/description/split correctly, but doesn't
  save the expense once validation passes.
- Suggested approach: a small SharedPreferences + JSON store, same pattern as `AuthStore.java`
  — use that file as the template.
- Needs: something like a `GroupStore` (save/list groups) and an `ExpenseStore` (save/list
  expenses per group).
- Then wire `GroupsHomeActivity`, `GroupBalancesActivity`, and `ExpenseHistoryActivity` to read
  from that store instead of their current static/hardcoded XML text.

### 2. Balance math

Once real expense data exists (#1), compute who-owes-whom instead of the hardcoded
`+$62.50`-style strings baked into the layouts. Needed on `GroupBalancesActivity` and
`SettleUpActivity`.

### 3. Real navigation based on data

- `GroupsHomeActivity.java`: every group card currently opens the same hardcoded
  `GroupBalancesActivity` regardless of which card was tapped (see the `groupCards` loop in
  `onCreate`). Needs to pass which group was actually tapped.
- `ConfirmJoinTripActivity` always displays "Cocoa Beach trip" regardless of the join code
  entered.

### 4. Testing

Only the stock Android template test stubs exist (`ExampleUnitTest`,
`ExampleInstrumentedTest`) — no real tests yet. Worth at least a couple of unit tests on the
balance math once it exists, and/or instrumented tests for the input-validation flows already
in place.

### 5. Minor polish

- A few buttons are still silent no-ops not tied to validation: `SettleUpActivity`'s
  "Mark settled" buttons, `ExpenseHistoryActivity`'s filter chips.
- `app/src/main/res/xml/data_extraction_rules.xml` has an unaddressed stock TODO comment about
  backup rules.

## For the demo video

- **Rotation**: works for all 11 screens; validation-error state now survives rotation too.
- **Multiple screens**: all 11 are reachable and wired.
- **Error handling / input sanitization**: demonstrated on sign-in (bad email format, wrong
  password), create-account (password mismatch, too short, duplicate email), new-group (empty
  name, invalid join code), add-expense (missing/invalid amount, empty description, nobody
  selected to split with).
- **Messaging**: Toasts and Snackbars are used across those same flows.
- **Data persistence**: ⚠️ not demonstrable yet beyond login — needs item #1 done first before
  recording that part of the video.

## Demo login

- Email: `demo@tally.app`
- Password: `tally1234`
