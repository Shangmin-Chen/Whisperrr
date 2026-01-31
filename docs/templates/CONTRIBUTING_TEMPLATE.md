# Contributing to Whisperrr

**Purpose:** Guidelines for contributing to the Whisperrr project  
**Audience:** Contributors, developers  
**Related Docs:** [Links to development and code style docs]

---

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [How to Contribute](#how-to-contribute)
3. [Development Workflow](#development-workflow)
4. [Code Style](#code-style)
5. [Testing](#testing)
6. [Documentation](#documentation)
7. [Pull Request Process](#pull-request-process)

---

## Code of Conduct

[Include or link to code of conduct]

### Our Standards

- Be respectful and inclusive
- Welcome newcomers
- Focus on constructive feedback

---

## How to Contribute

### Reporting Bugs

**Before Submitting:**
- Check if bug already exists
- Verify it's reproducible
- Gather relevant information

**Bug Report Template:**
```markdown
**Description:**
[Clear description of the bug]

**Steps to Reproduce:**
1. Step 1
2. Step 2
3. Step 3

**Expected Behavior:**
[What should happen]

**Actual Behavior:**
[What actually happens]

**Environment:**
- OS: [Operating system]
- Version: [Version number]
- Browser: [If applicable]

**Additional Context:**
[Any other relevant information]
```

### Suggesting Features

**Feature Request Template:**
```markdown
**Feature Description:**
[Clear description of the feature]

**Use Case:**
[Why this feature would be useful]

**Proposed Solution:**
[How you envision this working]

**Alternatives Considered:**
[Other approaches you've considered]
```

### Contributing Code

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write/update tests
5. Update documentation
6. Submit a pull request

---

## Development Workflow

### Setting Up Development Environment

See [Development Setup Guide](../getting-started/DEVELOPMENT_SETUP.md) for detailed instructions.

**Quick Start:**
```bash
# Clone repository
git clone https://github.com/your-org/whisperrr.git
cd whisperrr

# Install dependencies
# [Installation commands]

# Run tests
# [Test commands]
```

### Branch Naming

Use descriptive branch names:

- `feature/description` - New features
- `fix/description` - Bug fixes
- `docs/description` - Documentation updates
- `refactor/description` - Code refactoring

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): subject

body

footer
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test changes
- `chore`: Build/tooling changes

**Example:**
```
feat(api): add transcription endpoint

Add POST /api/audio/transcribe endpoint for audio transcription.
Includes file validation and error handling.

Closes #123
```

---

## Code Style

### General Guidelines

- Follow language-specific style guides
- Write clear, readable code
- Add comments for complex logic
- Keep functions focused and small

### Language-Specific Guidelines

#### Java

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use 4 spaces for indentation
- Maximum line length: 100 characters

#### TypeScript

- Follow [TypeScript Style Guide](https://github.com/basarat/typescript-book/blob/master/docs/styleguide/styleguide.md)
- Use 2 spaces for indentation
- Use meaningful variable names

#### Python

- Follow [PEP 8](https://www.python.org/dev/peps/pep-0008/)
- Use 4 spaces for indentation
- Maximum line length: 88 characters (Black formatter)

See [Code Style Guide](../development/CODE_STYLE.md) for detailed guidelines.

---

## Testing

### Test Requirements

- All new features must include tests
- Bug fixes must include regression tests
- Maintain or improve test coverage
- All tests must pass before submitting PR

### Running Tests

```bash
# Backend tests
cd backend && ./mvnw test

# Frontend tests
cd frontend && npm test

# Python service tests
cd python-service && pytest
```

### Writing Tests

- Write clear test names
- Test both success and failure cases
- Mock external dependencies
- Keep tests independent

See [Testing Guide](../development/TESTING.md) for detailed guidelines.

---

## Documentation

### Documentation Requirements

- Update documentation with code changes
- Add examples for new features
- Update API docs if APIs change
- Keep documentation current

### Documentation Style

Follow the [Documentation Style Guide](../meta/STYLE_GUIDE.md).

**Key Points:**
- Use clear, concise language
- Include code examples
- Add cross-references

---

## Pull Request Process

### Before Submitting

- [ ] Code follows style guide
- [ ] All tests pass
- [ ] Documentation updated
- [ ] Commit messages follow conventions
- [ ] No merge conflicts
- [ ] Changes are focused and logical

### PR Template

```markdown
## Description
[Clear description of changes]

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
[How you tested the changes]

## Checklist
- [ ] Code follows style guide
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
```

### Review Process

1. **Automated Checks:**
   - CI/CD pipeline runs tests
   - Linters check code style
   - Security scans run

2. **Code Review:**
   - At least one approval required
   - Address all review comments
   - Keep PR focused and reviewable

3. **Merge:**
   - Squash and merge (preferred)
   - Delete branch after merge
   - Update related issues

---

## Getting Help

### Questions?

- Check [Documentation](../README.md)
- Search [Issues](https://github.com/your-org/whisperrr/issues)
- Ask in [Discussions](https://github.com/your-org/whisperrr/discussions)

### Need Assistance?

- Open an issue for bugs
- Start a discussion for questions
- Contact maintainers for guidance

---

## Recognition

Contributors will be:
- Listed in CONTRIBUTORS.md
- Mentioned in release notes
- Credited in relevant documentation

---

## See Also

- [Code Style Guide](../development/CODE_STYLE.md)
- [Testing Guide](../development/TESTING.md)
- [Documentation Style Guide](../meta/STYLE_GUIDE.md)
- [Main Documentation Index](../README.md)

---

Thank you for contributing to Whisperrr! 🎉

